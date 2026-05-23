import { config } from '../config.js';

/**
 * Fire-and-forget FCM push. Never throws — logs and swallows errors so that
 * the main API request always succeeds even if FCM is misconfigured or down.
 */
export async function sendFcmNotification(fcmToken, title, body, data = {}) {
  if (!fcmToken || !config.fcmServerKey) return;
  try {
    const res = await fetch('https://fcm.googleapis.com/fcm/send', {
      method: 'POST',
      headers: {
        Authorization: `key=${config.fcmServerKey}`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        to: fcmToken,
        notification: { title, body },
        data,
      }),
    });
    if (!res.ok) {
      console.warn('[fcm] non-2xx response', res.status, await res.text());
    }
  } catch (err) {
    console.warn('[fcm] send failed', err?.message ?? err);
  }
}

/** Look up a user's fcm_token (helper). */
export async function getFcmToken(client, userId) {
  const { rows } = await client.query(
    'SELECT fcm_token FROM users WHERE id = $1',
    [userId],
  );
  return rows[0]?.fcm_token ?? null;
}
