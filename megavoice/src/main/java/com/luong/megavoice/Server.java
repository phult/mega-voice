/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luong.megavoice;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author phuluong
 */
public class Server {

    ServerSocket MyService;
    Socket clientSocket = null;
    InputStream input;
    AudioFormat audioFormat;
    SourceDataLine sourceDataLine;
    byte tempBuffer[] = new byte[10000];
    static Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

    Server() throws LineUnavailableException, IOException {

        try {
            Mixer mixer_ = AudioSystem.getMixer(mixerInfo[0]);
            audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            MyService = new ServerSocket(8000);
            clientSocket = MyService.accept();

            input = new BufferedInputStream(clientSocket.getInputStream());
            while (input.read(tempBuffer) != -1) {
                sourceDataLine.write(tempBuffer, 0, 10000);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;
        int sampleSizeInBits = 8;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    public static void main(String s[]) throws LineUnavailableException, IOException {
        Server s2 = new Server();
    }
}