package sparta.workout.controllers;

import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

class SoundPoolEvent {
	public SoundPoolEvent(int eventType, int eventSound) {
		this.eventType = eventType;
		this.eventSound = eventSound;
	}

	public int eventType;
	public int eventSound;

	public static final int SOUND_PLAY = 0;
	public static final int SOUND_STOP = 1;
	public static final int SOUND_MUSIC_PLAY = 2;
	public static final int SOUND_MUSIC_PAUSE = 3;
	public static final int SOUND_MUSIC_STOP = 4;
	public static final int SOUND_MUSIC_RESUME = 5;
}

class SoundStatus {
	public SoundStatus() {

	}

	public static final int STATUS_LOOPING_NOT_STARTED = 0;
	public static final int STATUS_LOOPING_PAUSED = 1;
	public static final int STATUS_LOOPING_PLAYING = 2;

}

public class SoundManager extends Thread implements iSoundManager {

	public int currentPlayer;
	private boolean isRunning;
	private java.util.HashMap<Integer, Boolean> sounds;
	private java.util.HashMap<Integer, Integer> handles;
	private android.content.Context context;
	private java.util.LinkedList soundEvents;
	private java.util.HashMap<Integer, MediaPlayer> mediaPlayers;

	public SoundManager(android.content.Context context) {
		this.context = context;
		soundEvents = new java.util.LinkedList();
		sounds = new java.util.HashMap();
		handles = new java.util.HashMap();
		mediaPlayers = new java.util.HashMap();
		isRunning = false;

	}

	public void addSound(int resid, boolean isLooping) {

		sounds.put(resid, new Boolean(isLooping));
		if (isLooping) {
			try {
				android.media.MediaPlayer mp = android.media.MediaPlayer.create(context, resid);
				mp.setLooping(true);
				mp.seekTo(0);
				// mp.prepare();
				mediaPlayers.put(resid, mp);

				// mp.seekTo(0);
				// mp.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
			} catch (Exception e) {
				Log.d("ERROR", e.getMessage());
			}

		}
	}

	public void run() {
		android.media.MediaPlayer mp = null;
		while (isRunning) {
			try {
				while (soundEvents.size() > 0) {
					SoundPoolEvent event = (SoundPoolEvent) soundEvents.remove();
					if (event != null) {
						switch (event.eventType) {
						case SoundPoolEvent.SOUND_PLAY:
							android.media.AudioManager mgr = (android.media.AudioManager) context.getSystemService(android.content.Context.AUDIO_SERVICE);
							int streamVolume = mgr.getStreamVolume(android.media.AudioManager.STREAM_MUSIC);
							soundPool.play(handles.get(event.eventSound).intValue(), streamVolume, streamVolume, 1, 0, 1.0f);

							break;
						case SoundPoolEvent.SOUND_STOP:

							break;
						case SoundPoolEvent.SOUND_MUSIC_PLAY:
							currentPlayer = event.eventSound;
							if (sounds.get(currentPlayer) != null) {
								mp = (MediaPlayer) mediaPlayers.get(currentPlayer);

								if (!mp.isPlaying()) {
									mp.seekTo(0);
									mp.start();
								}
							}

							break;
						case SoundPoolEvent.SOUND_MUSIC_STOP:
							currentPlayer = event.eventSound;
							mp = mediaPlayers.get(currentPlayer);
							mp.pause();
							break;
						case SoundPoolEvent.SOUND_MUSIC_PAUSE:
							currentPlayer = event.eventSound;
							mp = mediaPlayers.get(currentPlayer);
							mp.pause();
							break;
						case SoundPoolEvent.SOUND_MUSIC_RESUME:
							currentPlayer = event.eventSound;
							mp = mediaPlayers.get(currentPlayer);
							mp.start();
							break;

						}

					}
				}
			} catch (Exception e) {
				// Log.d("Error",e.getMessage());
			}

			try {
				this.wait(100);
				// java.lang.Thread.currentThread().sleep(100);
			} catch (Exception e) {

			}
		}
	}

	public void startSound() {
		this.soundPool = new android.media.SoundPool(this.sounds.size(), android.media.AudioManager.STREAM_MUSIC, 100);
		java.util.Iterator<Integer> iterator = sounds.keySet().iterator();

		while (iterator.hasNext()) {
			int soundid = iterator.next().intValue();
			int soundhandle = this.soundPool.load(this.context, soundid, 1);
			handles.put(new Integer(soundid), new Integer(soundhandle));
		}

		isRunning = true;
		this.start();
	}

	public void stopSound() {
		java.util.Iterator<Integer> iterator = sounds.keySet().iterator();

		while (iterator.hasNext()) {

			int soundid = iterator.next().intValue();
			if (this.sounds.get(soundid).booleanValue()) {
				android.media.MediaPlayer mp = mediaPlayers.get(soundid);
				mp.stop();
				mp.release();
				mp = null;
			} else {
				this.soundPool.pause(this.handles.get(soundid).intValue());
				this.soundPool.stop(this.handles.get(soundid).intValue());

			}

		}

		isRunning = false;
		this.soundPool.release();
	}

	public void stopSound(int resid) {
		if (soundEvents != null) {
			try {
				soundEvents.add(new SoundPoolEvent(SoundPoolEvent.SOUND_STOP, resid));
			} catch (Exception e) {

			}
		}
	}

	public void playSound(int resid) {
		if (soundEvents != null) {
			try {
				soundEvents.add(new SoundPoolEvent(SoundPoolEvent.SOUND_PLAY, resid));
				this.notify();
			} catch (Exception e) {

			}
		}
	}

	public void startMusic(int resid) {
		if (soundEvents != null) {
			try {
				soundEvents.add(new SoundPoolEvent(SoundPoolEvent.SOUND_MUSIC_PLAY, resid));
				this.notify();
			} catch (Exception e) {

			}
		}
	}

	public void stopMusic(int resid) {
		if (soundEvents != null) {
			try {
				soundEvents.add(new SoundPoolEvent(SoundPoolEvent.SOUND_MUSIC_STOP, resid));
				this.notify();
			} catch (Exception e) {

			}
		}
	}

	public void pauseMusic(int resid) {
		if (soundEvents != null) {
			try {
				soundEvents.add(new SoundPoolEvent(SoundPoolEvent.SOUND_MUSIC_PAUSE, resid));
				this.notify();
			} catch (Exception e) {

			}
		}
	}

	public void resumeMusic(int resid) {
		if (soundEvents != null) {
			try {
				soundEvents.add(new SoundPoolEvent(SoundPoolEvent.SOUND_MUSIC_RESUME, resid));
				this.notify();
			} catch (Exception e) {

			}
		}
	}

	SoundPool soundPool;

}