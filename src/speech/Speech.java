/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package speech;
import java.beans.PropertyVetoException;
import java.util.Locale;
import java.util.Scanner;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;

public class Speech {
 
  SynthesizerModeDesc desc;
  Synthesizer synthesizer;
  Voice voice;
  

  public void init(String voiceName) 
    throws EngineException, AudioException, EngineStateError, 
           PropertyVetoException 
  {
    if (desc == null) {
      
      System.setProperty("freetts.voices", 
        "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
      
      desc = new SynthesizerModeDesc(Locale.US);
      Central.registerEngineCentral
        ("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
      synthesizer = Central.createSynthesizer(desc);
      synthesizer.allocate();
      synthesizer.resume();
      SynthesizerModeDesc smd = 
        (SynthesizerModeDesc)synthesizer.getEngineModeDesc();
      Voice[] voices = smd.getVoices();
      Voice voice = null;
      voices[0].setGender(Voice.GENDER_FEMALE);
      voices[1].setGender(Voice.GENDER_FEMALE);
      for(int i = 0; i < voices.length; i++) {
           System.out.println(voices[i].getName());
        if(voices[i].getName().equals(voiceName)) {
           
          voice = voices[i];
          break;
        }
      }
      synthesizer.getSynthesizerProperties().setVoice(voice);
      //synthesizer.getSynthesizerProperties().setPitch(200f);
      //synthesizer.getSynthesizerProperties().setSpeakingRate(100f);
    }
    
  }

  public void terminate() throws EngineException, EngineStateError {
    synthesizer.deallocate();
  }
  
  public void doSpeak(String speakText) 
    throws EngineException, AudioException, IllegalArgumentException, 
           InterruptedException 
  {
      synthesizer.speakPlainText(speakText, null);
      synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);

  }
  
  
  public static void main (String[]args) throws Exception{
    Speech su = new Speech();
    
    su.init("kevin16");
    su.init(null);
    Scanner s = new Scanner(System.in);
    boolean run = true;
    String speech;
    while(run) {
        speech = s.nextLine();
        if (speech.equals("Stop")) {
            run = false;
        }
        else
            su.doSpeak(speech);
        
    }
    // high quality
    
    su.terminate();
  }
} 