/* eslint-disable no-unused-vars */
export class HttpError extends Error {
  constructor(status, message, details) {
    super(message);
    this.status = status;
    this.details = details;
  }
}

export function notFound(_req, res, _next) {
  res.status(404).json({ error: 'not_found' });
}

export function errorHandler(err, _req, res, _next) {
  if (err instanceof HttpError) {
    return res.status(err.status).json({ error: err.message, details: err.details });
  }
  console.error('[error]', err);
  res.status(500).json({ error: 'internal_error' });
}
