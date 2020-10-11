/*
Library to synthesize voice.
Usage: 
	Create necessary directories for Voice to function:
		LIBRARY Voice INITIALIZE
	Generate wav file:
		LIBRARY Voice [voicebank name] [filename] [text]
	Play wav file:
		LIBRARY Voice PLAY [filename]
@author Abir Haque
@version 1.1.0
@since CSVOL 21_0.0.0
*/
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;
import java.math.*;

public class Voice
{
	public static void main(ArrayList<String> args, ArrayList<String> originalArgs) throws Exception
  	{
    	ArrayList<String> arguments = originalArgs;
        
		if ((arguments.get(2)).equals("INITIALIZE"))
		{
			initialize();
		}
		if ((arguments.get(2)).equals("PLAY"))
		{
			play(arguments);
		}
		else
		{
    			fallBack(arguments);
		}
  	}
	public static void initialize() throws Exception
  	{
			File dir1 = new File("voice");
			File dir2 = new File("voice/recordings");
			File dir3 = new File("voice/temps");
			File dir4 = new File("voice/voicebanks");
			dir1.mkdirs();
			dir2.mkdirs();
			dir3.mkdirs();
			dir4.mkdirs();
		}
	public static void play(ArrayList<String> args) throws Exception
  	{
		ArrayList<String> arguments = args;
		String recordingLocation = "voice/recordings/";
		String recording = arguments.get(3);
		String format = ".wav";
		Clip clip = AudioSystem.getClip();
        	clip.open(AudioSystem.getAudioInputStream(new File(recordingLocation+recording+format)));
        	clip.start();
		while (!clip.isRunning())
		{
   			Thread.sleep(1);
		}
		while (clip.isRunning())
		{
   			Thread.sleep(1);
		}
		clip.close();
	}
  	public static void fallBack(ArrayList<String> args) throws Exception
  	{
		ArrayList<String> arguments = args;

		String csvolLocation = System.getProperty("user.dir") + "/";
    		String bankLocation = "voice/voicebanks/";
		String bankName= arguments.get(2)+"/";
		String location = bankLocation + bankName;
		String format = ".wav";
		String productLocation = "voice/recordings/";
		String productName = arguments.get(3);
		File output = new File(productLocation+productName + format);
		int frequency = 0;

		String sentence = "";
    		for (int i = 4; i < arguments.size(); i++)
		{
			if (i == arguments.size()-1)
			{
				sentence = sentence + arguments.get(i);
			}
			else
			{
				sentence = sentence + arguments.get(i) + " ";
			}
		}


		ArrayList<String> letters = new ArrayList<String>();
		ArrayList<String> vowels = new ArrayList<String>();
		ArrayList<Integer> frequencyMarkers = new ArrayList<Integer>();
		String locale = "EN";
		switch (locale)
		{
			case "EN":
				sentence = sentence.replaceAll("\\s","_");
				vowels.add("a");
				vowels.add("e");
				vowels.add("ee");
				vowels.add("i");
				vowels.add("o");
				vowels.add("oo");
				vowels.add("u");
				int i = 0;
        			while(i<sentence.length())
				{ 	
					if ((sentence.substring(i,i+1)).equals("-"))
					{	
						if(vowels.contains(sentence.substring(i-1,i)))	
						{
							letters.add(sentence.substring(i-1,i));	
							String half1 = sentence.substring(0,i-1);	
							String before = sentence.substring(i-1,i);	
							String mid = sentence.substring(i-1,i);	
							String half2 = sentence.substring(i+1,sentence.length());
							sentence = half1 + before + mid + half2;
							frequencyMarkers.add(frequency);
						}
						else
						{
							letters.add("_");
							frequencyMarkers.add(0);
						}
						i++;
						continue;
					}
					if ((sentence.substring(i,i+1)).equals("$") && (Character.isDigit(sentence.charAt(i+1))))
					{
						ArrayList<Integer> places = new ArrayList<Integer>();
						int subIndex = i+1;
						while(Character.isDigit(sentence.charAt(subIndex)))
						{
							places.add(Integer.parseInt(sentence.substring(subIndex,subIndex+1)));
							i++;
							subIndex++;
						}
						int breaks = 0;
						for (int k = 0; k<places.size(); k++)
						{
							breaks = breaks+((places.get(k))*(int)(Math.pow(10,places.size()-1-k)));
						}
						for (int k = 0; k < breaks; k++)
						{
							letters.add("_");
							frequencyMarkers.add(0);
						}
						i++;
						continue;
					}
					if ((sentence.substring(i,i+1)).equals("^") && (Character.isDigit(sentence.charAt(i+2))))
					{
						if (sentence.substring(i+1,i+2).equals("+"))
						{
							ArrayList<Integer> places = new ArrayList<Integer>();
							int subIndex = i+2;
							while(Character.isDigit(sentence.charAt(subIndex)))
							{
								places.add(Integer.parseInt(sentence.substring(subIndex,subIndex+1)));
								i++;
								subIndex++;
							}
							int breaks = 0;
							for (int k = 0; k<places.size(); k++)
							{
								breaks = breaks+((places.get(k))*(int)(Math.pow(10,places.size()-1-k)));
							}
							frequency = breaks;
						}
						if (sentence.substring(i+1,i+2).equals("-"))
						{
							ArrayList<Integer> places = new ArrayList<Integer>();
							int subIndex = i+2;
							while(Character.isDigit(sentence.charAt(subIndex)))
							{
								places.add(Integer.parseInt(sentence.substring(subIndex,subIndex+1)));
								i++;
								subIndex++;
							}
							int breaks = 0;
							for (int k = 0; k<places.size(); k++)
							{
								breaks = breaks+((places.get(k))*(int)(Math.pow(10,places.size()-1-k)));
							}
							breaks = breaks * -1;
							frequency = breaks;
						}					
						i = i + 2;
						continue;
					}
					if ((sentence.substring(i,i+1)).equals("a") && (sentence.substring(i+1,i+2)).equals("i"))
					{
						letters.add("e");
						letters.add("i");
						frequencyMarkers.add(frequency);
						frequencyMarkers.add(frequency);
						i = i + 2;
						continue;
					}
					if ((sentence.substring(i,i+1)).equals("e") && (sentence.substring(i+1,i+2)).equals("e"))
					{
						letters.add("i");
						frequencyMarkers.add(frequency);
						i = i + 2;
						continue;
					}
					if ((sentence.substring(i,i+1)).equals("o") && (sentence.substring(i+1,i+2)).equals("o"))
					{
						letters.add("oo");
						frequencyMarkers.add(frequency);
						i = i + 2;
						continue;
					}
					if ((sentence.substring(i,i+1)).equals("n") && (sentence.substring(i+1,i+2)).equals("n"))
					{
						letters.add("n");
						frequencyMarkers.add(frequency);
						i = i + 2;
						continue;
					}
					else
					{
						letters.add(sentence.substring(i,i+1));
						frequencyMarkers.add(frequency);
						i++;
						continue;
					}
       	 			}
				break;
			default:
				sentence = sentence.replaceAll("\\s","_");
       	 			for (int k = 0; k < sentence.length(); k++) 
				{ 	
					letters.add(sentence.substring(k,k+1));
       	 			}
			}
		AudioInputStream[] clips = new AudioInputStream[letters.size()];
		Vector v = new Vector();
		long length = 0;
		int vibrato = 1;
		for (int i = 0; i < letters.size(); i++)
		{
			letters.set(i,location + letters.get(i) + format);
			if(i != 0 && (letters.get(i)).equals(letters.get(i-1)))
			{
				File tempFile = new File("voice/temps/temp"+i+format);
				tempFile.createNewFile();
				InputStream ain = new BufferedInputStream(
new FileInputStream(new File(letters.get(i))));

				OutputStream aout = new BufferedOutputStream(
new FileOutputStream(tempFile));      
				byte[] buffer = new byte[1024];
        
				int lengthRead;
        
				while ((lengthRead = ain.read(buffer)) > 0) 
				{

					aout.write(buffer, 0, lengthRead);
					aout.flush();
   
				}
				aout.close();
				if (frequencyMarkers.get(i) != 0)
				{				
					double div = 1;
					double factor = 1.05946309436;//2^(1/12)//1.059
					if (frequencyMarkers.get(i) > 0)
					{
						for(int k = 0; k < frequencyMarkers.get(i); k++)
						{
							div = div / factor;
						}
					}
					else
					{
						for(int k = 0; k < -1*frequencyMarkers.get(i); k++)
						{
							div = div * factor;
						}
					}
					
					BigDecimal tempDiv = new BigDecimal(Double.toString(div));
					BigDecimal roundedDiv = tempDiv.setScale(2, BigDecimal.ROUND_HALF_EVEN);

					BigDecimal tempPow = new BigDecimal(Double.toString(44100*(Math.pow(factor, frequencyMarkers.get(i)))));
					BigDecimal roundedPow = tempPow.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					
					try
					{
						/*Process p =Runtime.getRuntime().exec("ffmpeg -y -i " + csvolLocation + letters.get(i) + " -af asetrate=" + roundedPow.toString() + ",aresample=44100,atempo=" + roundedDiv.toString() + " " + csvolLocation + "voice/temps/temp" + i + format, null, new File(csvolLocation + "voice/ffmpeg/bin/"));*/
						Process p =Runtime.getRuntime().exec("ffmpeg -y -i " + csvolLocation + letters.get(i) + " -af asetrate=" + roundedPow.toString() + ",aresample=44100,atempo=" + roundedDiv.toString() + " " + csvolLocation + "voice/temps/temp" + i + format);
					}
					catch(Exception e)
					{
						Terminal.error();
						System.out.println("FFmpeg is not installed on your machine. FFmpeg is needed to change the pitch of audio files. Please do the following in order to have CSVOL Voice running with FFmpeg.\n1. Head to https://ffmpeg.org/download.html to find an appropriate binary download for your system.\n2. Locate the directory containing ffmpeg.exe (most likely in a 'bin' folder).\n3. Paste that directory in your environment variables.\nWhy do I have to do this?:\nI, Abir Haque personally apologize for not including FFmpeg in your version of the CSVOL Voice library. As of 10 October 2020, I did not include FFmpeg as it is protected under the GNU Lesser General Public License (LGPL). To remain compliant with the LGPL, I either must follow the license compliance checklist located at https://ffmpeg.org/legal.html, or force the end-user, you, to download and configure FFmpeg for CSVOL. I am not against following the checklist. In fact, I plan on following the the checklist to include FFmpeg in future version of the CSVOL Voice library.");
					}
					Thread.sleep(300);

					clips[i] = AudioSystem.getAudioInputStream(tempFile);
					length = length + (int)(clips[i].getFrameLength());
				}
				else
				{
					clips[i] = AudioSystem.getAudioInputStream(tempFile);
					length = length + (int)(clips[i].getFrameLength());
					vibrato = 1;	
				}		
			}
			else
			{	
				File tempFile = new File("voice/temps/temp"+i+format);
				tempFile.createNewFile();
				InputStream ain = new BufferedInputStream(
new FileInputStream(new File(letters.get(i))));

				OutputStream aout = new BufferedOutputStream(
new FileOutputStream(tempFile));      
				byte[] buffer = new byte[1024];
        
				int lengthRead;
        
				while ((lengthRead = ain.read(buffer)) > 0) 
				{

					aout.write(buffer, 0, lengthRead);
					aout.flush();
   
				}
				aout.close();

	
				if (frequencyMarkers.get(i) != 0)
				{
					double div = 1;
					double factor = 1.05946309436;//2^(1/12)//1.059
					if (frequencyMarkers.get(i) > 0)
					{
						for(int k = 0; k < frequencyMarkers.get(i); k++)
						{
							div = div / factor;
						}
					}
					else
					{
						for(int k = 0; k < -1*frequencyMarkers.get(i); k++)
						{
							div = div * factor;
						}
					}
				
					BigDecimal tempDiv = new BigDecimal(Double.toString(div));
					BigDecimal roundedDiv = tempDiv.setScale(2, BigDecimal.ROUND_HALF_EVEN);

					BigDecimal tempPow = new BigDecimal(Double.toString(44100*(Math.pow(factor, frequencyMarkers.get(i)))));
					BigDecimal roundedPow = tempPow.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				
					try
					{
						/*Process p =Runtime.getRuntime().exec("ffmpeg -y -i " + csvolLocation + letters.get(i) + " -af asetrate=" + roundedPow.toString() + ",aresample=44100,atempo=" + roundedDiv.toString() + " " + csvolLocation + "voice/temps/temp" + i + format, null, new File(csvolLocation + "voice/ffmpeg/bin/"));*/
						Process p =Runtime.getRuntime().exec("ffmpeg -y -i " + csvolLocation + letters.get(i) + " -af asetrate=" + roundedPow.toString() + ",aresample=44100,atempo=" + roundedDiv.toString() + " " + csvolLocation + "voice/temps/temp" + i + format);
					}
					catch(Exception e)
					{
						Terminal.error();
						System.out.println("FFmpeg is not installed on your machine. FFmpeg is needed to change the pitch of audio files. Please do the following in order to have CSVOL Voice running with FFmpeg.\n1. Head to https://ffmpeg.org/download.html to find an appropriate binary download for your system.\n2. Locate the directory containing ffmpeg.exe (most likely in a 'bin' folder).\n3. Paste that directory in your environment variables.\nWhy do I have to do this?:\nI, Abir Haque personally apologize for not including FFmpeg in your version of the CSVOL Voice library. As of 10 October 2020, I did not include FFmpeg as it is protected under the GNU Lesser General Public License (LGPL). To remain compliant with the LGPL, I either must follow the license compliance checklist located at https://ffmpeg.org/legal.html, or force the end-user, you, to download and configure FFmpeg for CSVOL. I am not against following the checklist. In fact, I plan on following the the checklist to include FFmpeg in future version of the CSVOL Voice library.");
					}
					Thread.sleep(300);
				}
				clips[i] = AudioSystem.getAudioInputStream(tempFile);
				length = length + clips[i].getFrameLength();
			}
			v.add(clips[i]);
			double made= i+1;
			double total = clips.length;
			/*String bar = "[";
			for (int k = 0; k < made; k++)
			{
				if (k % 10 == 0)
				{
					bar = bar + "|";
				}
			}
			for (int k = 0; k < total - made; k++)
			{
				if (k % 10 == 0)
				{
					bar = bar + " ";
				}
			}
			bar = bar + "] ";*/
			int progress = (int) ((made/total)*100);		
			System.out.print("\rProgress: " + progress +"%");// + bar);
		}
		Enumeration enumeration = v.elements(); 
		SequenceInputStream sin = new SequenceInputStream(enumeration);
      		AudioInputStream appendedFiles = new AudioInputStream(sin, clips[0].getFormat(), length);
      		AudioSystem.write(appendedFiles, AudioFileFormat.Type.WAVE, output);
		System.out.println();
		Thread.sleep(1000);
	}
}