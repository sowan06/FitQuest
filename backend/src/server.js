import { app } from './app.js';
import { config } from './config.js';
import { startStreakCron } from './services/streakCron.js';

const server = app.listen(config.port, () => {
  console.log(`[fitquest] listening on :${config.port} (${config.nodeEnv})`);
});

startStreakCron();

function shutdown(signal) {
  console.log(`[fitquest] received ${signal}, shutting down`);
  server.close(() => process.exit(0));
}

process.on('SIGTERM', () => shutdown('SIGTERM'));
process.on('SIGINT', () => shutdown('SIGINT'));
