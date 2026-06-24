// ════════════════════════════════════════════════════════════
// Servidor BFA — Test Espacial
// Recibe resultados del HTML y los guarda en PostgreSQL.
// ════════════════════════════════════════════════════════════

const express = require('express');
const cors = require('cors');
const bcrypt = require('bcrypt');
const { Pool } = require('pg');

const app = express();
app.use(cors());
app.use(express.json());

// ── Conexión a PostgreSQL ───────────────────────────────────
// AJUSTA estos valores con tus credenciales reales de pgAdmin.
const pool = new Pool({
  host: 'localhost',
  port: 5432,
  database: 'bfa_db',
  user: 'postgres',
  password: '1234',   // <-- cambia esto
});

pool.connect()
  .then(() => console.log('✅ Conectado a PostgreSQL (bfa_db)'))
  .catch(err => console.error('❌ Error conectando a PostgreSQL:', err.message));

// ── Psicólogo genérico para tests autoadministrados ──────────
// AJUSTA este valor con el id_usuario real que devolvió el INSERT
// del psicólogo "Sistema - Autoadministrado" en la base de datos.
const ID_PSICOLOGO_SISTEMA = 1;
const SALT_ROUNDS = 10;

// ── Endpoint: listar psicólogos (para selectores/admin) ──────
app.get('/api/psicologos', async (req, res) => {
  try {
    const result = await pool.query(
      `SELECT id_usuario, nombre, codigo_colegiado, especialidad
       FROM psicologo
       ORDER BY nombre`
    );
    res.json(result.rows);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Error al obtener psicólogos' });
  }
});

// ── Endpoint: login de psicólogo ──────────────────────────────
// Body esperado: { nombre_usuario: "jroman", password: "123456" }
app.post('/api/psicologos/login', async (req, res) => {
  const { nombre_usuario, password } = req.body;

  if (!nombre_usuario || !password) {
    return res.status(400).json({ error: 'Usuario y contraseña son requeridos' });
  }

  try {
    const result = await pool.query(
      `SELECT id_usuario, nombre, codigo_colegiado, especialidad, password_hash
       FROM psicologo
       WHERE nombre_usuario = $1`,
      [nombre_usuario]
    );

    if (result.rows.length === 0) {
      return res.status(401).json({ error: 'Usuario o contraseña incorrectos' });
    }

    const psicologo = result.rows[0];

    if (!psicologo.password_hash) {
      return res.status(401).json({ error: 'Este usuario no tiene contraseña configurada' });
    }

    const passwordValida = await bcrypt.compare(password, psicologo.password_hash);
    if (!passwordValida) {
      return res.status(401).json({ error: 'Usuario o contraseña incorrectos' });
    }

    // Nunca devolver el hash al cliente.
    res.json({
      id_usuario: psicologo.id_usuario,
      nombre: psicologo.nombre,
      codigo_colegiado: psicologo.codigo_colegiado,
      especialidad: psicologo.especialidad
    });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Error al iniciar sesión' });
  }
});

// ── Endpoint: registrar credenciales para un psicólogo existente ──
// Útil para asignarle nombre_usuario + contraseña a un psicólogo que
// ya fue creado desde OpenXava (que no maneja el hash de bcrypt).
// Body esperado: { id_usuario: 2, nombre_usuario: "jroman", password: "123456" }
app.post('/api/psicologos/registrar-credenciales', async (req, res) => {
  const { id_usuario, nombre_usuario, password } = req.body;

  if (!id_usuario || !nombre_usuario || !password) {
    return res.status(400).json({ error: 'Datos incompletos' });
  }

  try {
    const hash = await bcrypt.hash(password, SALT_ROUNDS);
    const result = await pool.query(
      `UPDATE psicologo
       SET nombre_usuario = $1, password_hash = $2
       WHERE id_usuario = $3
       RETURNING id_usuario, nombre`,
      [nombre_usuario, hash, id_usuario]
    );

    if (result.rows.length === 0) {
      return res.status(404).json({ error: 'Psicólogo no encontrado' });
    }

    res.json({ ok: true, psicologo: result.rows[0] });
  } catch (err) {
    console.error(err);
    if (err.code === '23505') { // unique_violation
      return res.status(409).json({ error: 'Ese nombre de usuario ya está en uso' });
    }
    res.status(500).json({ error: 'Error al registrar credenciales' });
  }
});

// ── Endpoint: obtener las preguntas del Test Espacial ────────
// El HTML puede usar esto en vez de tener las respuestas correctas
// hardcodeadas en el front (más seguro).
app.get('/api/test-espacial/preguntas', async (req, res) => {
  try {
    const result = await pool.query(
      `SELECT p.id_pregunta, p.numero_pregunta, p.enunciado
       FROM pregunta p
       JOIN test_espacial te ON p.id_test = te.id_test
       ORDER BY p.numero_pregunta`
    );
    res.json(result.rows);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Error al obtener preguntas' });
  }
});

// ── Endpoint: crear sujeto (si no existe, por correo) ─────────
app.post('/api/sujetos', async (req, res) => {
  const { nombre, correo, edad, genero } = req.body;
  try {
    const existing = await pool.query('SELECT id_usuario FROM sujeto WHERE correo = $1', [correo]);
    if (existing.rows.length > 0) {
      return res.json({ id_usuario: existing.rows[0].id_usuario });
    }
    const result = await pool.query(
      `INSERT INTO sujeto (nombre, correo, edad, genero)
       VALUES ($1, $2, $3, $4) RETURNING id_usuario`,
      [nombre, correo, edad || null, genero || 'No especificado']
    );
    res.json({ id_usuario: result.rows[0].id_usuario });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Error al crear sujeto' });
  }
});

// ── Endpoint: guardar el resultado completo de una sesión ─────
// Body esperado:
// {
//   id_sujeto: 1,
//   id_psicologo: 2,           (opcional; si no llega, se usa el genérico)
//   respuestas: [{ id_pregunta: 10, respuesta_marcada: "A", es_correcta: true }, ...],
//   tiempo_segundos: 245
// }
app.post('/api/test-espacial/resultado', async (req, res) => {
  const { id_sujeto, id_psicologo, respuestas, tiempo_segundos } = req.body;

  if (!id_sujeto || !Array.isArray(respuestas)) {
    return res.status(400).json({ error: 'Datos incompletos' });
  }

  const idPsicologoFinal = id_psicologo || ID_PSICOLOGO_SISTEMA;

  const client = await pool.connect();
  try {
    await client.query('BEGIN');

    // 1) Crear la sesión de evaluación
    const sesionResult = await client.query(
      `INSERT INTO sesion_evaluacion (estado, fecha_inicio, fecha_fin, finalidad, id_sujeto, id_psicologo)
       VALUES ('FINALIZADA', NOW() - INTERVAL '1 second' * $1, NOW(), 'Test Espacial', $2, $3)
       RETURNING id_sesion`,
      [tiempo_segundos, id_sujeto, idPsicologoFinal]
    );
    const idSesion = sesionResult.rows[0].id_sesion;

    // 2) Guardar cada respuesta
    let correctas = 0, incorrectas = 0, omitidas = 0;
    for (const r of respuestas) {
      const estado = r.respuesta_marcada ? 'RESPONDIDA' : 'OMITIDA';
      if (!r.respuesta_marcada) omitidas++;
      else if (r.es_correcta) correctas++;
      else incorrectas++;

      await client.query(
        `INSERT INTO respuesta_sujeto (es_correcta, estado, fecha_inicio, respuesta_marcada, id_sesion, id_pregunta)
         VALUES ($1, $2, NOW(), $3, $4, $5)`,
        [r.es_correcta || false, estado, r.respuesta_marcada || null, idSesion, r.id_pregunta]
      );
    }

    // 3) Guardar el resumen del resultado
    const puntajeDirecto = correctas;
    const percentil = Math.min(puntajeDirecto * 5, 99); // placeholder, ajustar con baremo real
    const puntuacionTipica = percentil < 40 ? 4 : percentil < 77 ? 6 : 8;

    await client.query(
      `INSERT INTO resultado_test
         (id_sesion, puntaje_directo, total_correctas, total_incorrectas, total_omitidas,
          tiempo_segundos, percentil, puntuacion_tipica)
       VALUES ($1, $2, $3, $4, $5, $6, $7, $8)`,
      [idSesion, puntajeDirecto, correctas, incorrectas, omitidas, tiempo_segundos, percentil, puntuacionTipica]
    );

    await client.query('COMMIT');
    res.json({
      ok: true,
      id_sesion: idSesion,
      puntaje_directo: puntajeDirecto,
      correctas, incorrectas, omitidas,
      percentil, puntuacion_tipica: puntuacionTipica
    });
  } catch (err) {
    await client.query('ROLLBACK');
    console.error(err);
    res.status(500).json({ error: 'Error al guardar resultado' });
  } finally {
    client.release();
  }
});

// ── Endpoint: listar resultados (para revisión rápida) ─────────
// Soporta filtro opcional ?correo=... para buscar un sujeto específico.
app.get('/api/test-espacial/resultados', async (req, res) => {
  const { correo } = req.query;
  try {
    const params = [];
    let whereClause = '';
    if (correo) {
      params.push(`%${correo}%`);
      whereClause = `WHERE s.correo ILIKE $1 OR s.nombre ILIKE $1`;
    }
    const result = await pool.query(
      `SELECT rt.*, s.nombre, s.correo, p.nombre AS nombre_psicologo
       FROM resultado_test rt
       JOIN sesion_evaluacion se ON rt.id_sesion = se.id_sesion
       JOIN sujeto s ON se.id_sujeto = s.id_usuario
       LEFT JOIN psicologo p ON se.id_psicologo = p.id_usuario
       ${whereClause}
       ORDER BY rt.fecha_generacion DESC`,
      params
    );
    res.json(result.rows);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Error al obtener resultados' });
  }
});

const PORT = 3000;
app.listen(PORT, () => console.log(`🚀 Servidor BFA corriendo en http://localhost:${PORT}`));
