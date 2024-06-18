package org.example;

import java.util.Arrays;
import java.util.List;
import javafx.scene.media.Media;

/**
 * @Classname MusicLoader
 * @Description
 * @Date 2024-6-18 16:16
 * @Created by zoujie
 */
public class MusicLoader {
  public List<Media> loadMedia(){
    String netMusic1 = "http://music.163.com/song/media/outer/url?id=2074987490.mp3";
    String netMusic2 = "http://music.163.com/song/media/outer/url?id=1932055348.mp3";
    String netMusic3 = "http://music.163.com/song/media/outer/url?id=1836682277.mp3";
    return Arrays.asList(new Media(netMusic1),new Media(netMusic2),new Media(netMusic3));
  }
}
