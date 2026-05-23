import express from 'express';
import { errorHandler, notFound } from './middleware/errorHandler.js';
import authRoutes from './routes/auth.js';
import characterRoutes from './routes/character.js';
import workoutRoutes from './routes/workouts.js';
import foodRoutes from './routes/food.js';
import questRoutes from './routes/quests.js';

export function createApp() {
  const app = express();

  app.use(express.json({ limit: '256kb' }));

  app.get('/health', (_req, res) => {
    res.json({ status: 'ok' });
  });

  app.use('/auth', authRoutes);
  app.use('/character', characterRoutes);
  app.use('/workouts', workoutRoutes);
  app.use('/food', foodRoutes);
  app.use('/quests', questRoutes);
  app.use('/achievements', questRoutes); // achievement listing handled in quests router

  app.use(notFound);
  app.use(errorHandler);

  return app;
}

export const app = createApp();
