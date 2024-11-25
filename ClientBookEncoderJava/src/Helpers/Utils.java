package Helpers;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;


public class Utils {

    public static void ViderLesChamps(JTextField txtTitle, JTextField txtISBN, JSpinner spPages, JSpinner spPrix, JSpinner spAnnepubli, JSpinner spStock) {
        txtTitle.setText("");
        txtISBN.setText("");
        spPages.setValue(0);
        spPrix.setValue(0.0);
        spAnnepubli.setValue(1980);
        spStock.setValue(0);

        try {
            PlayMusic(6);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkDate(String date) {
        if (date.length() != 10) {
            return false;
        }
        String[] parts = date.split("-");
        if (parts.length != 3) {
            return false;
        }
        try {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            if (year < 1000 || year > 9999) {
                return false;
            }
            if (month < 1 || month > 12) {
                return false;
            }
            if (day < 1 || day > 31) {
                return false;
            }
            if (month == 2) {
                if (year % 4 == 0) {
                    if (day > 29) {
                        return false;
                    }
                } else {
                    if (day > 28) {
                        return false;
                    }
                }
            }
            if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day > 30) {
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;

    }

    public static boolean checkISBN(String isbn) {
        if (isbn.length() != 10) {
            return false;
        }
        try {
            Long.parseLong(isbn);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static void PlayMusic(int i) throws IOException, UnsupportedAudioFileException {
            String path = "";
            if (i == 0) {
                path = "ClientBookEncoderJava/Music/AddSong.wav";
            } else if (i == 1) {
                path = "ClientBookEncoderJava/Music/ErrorSong.wav";
            } else if (i == 2) {
                path = "ClientBookEncoderJava/Music/Logout.wav";
            } else if (i == 3) {
                path = "ClientBookEncoderJava/Music/ClickSong.wav";
            } else if (i == 4) {
                path = "ClientBookEncoderJava/Music/LoginSong.wav";
            } else if (i == 5) {
                path = "ClientBookEncoderJava/Music/LogoutSong.wav";
            } else if (i == 6) {
                path = "ClientBookEncoderJava/Music/ViderSong.wav";
            }
            File audioFile = new File(path);
            if (!audioFile.exists()) {
                throw new IOException("Audio file not found: " + path);
            }

            try(AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
                throw new IOException("Error playing audio file: " + path, e);
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
                throw new UnsupportedAudioFileException("Unsupported audio file: " + path);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Error reading audio file: " + path, e);
            }
    }


}
