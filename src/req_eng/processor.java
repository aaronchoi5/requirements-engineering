package req_eng;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.ArrayList;
import java.util.Scanner;

public class processor
{
	public static double countSimilarities(String[] arrX, String[] arrY, double numSimilarities)
	{
		for(int x = 0; x < arrX.length; x++)
		{
			for(int y = 0; y < arrY.length; y++)
			{
				if(arrX[x].equalsIgnoreCase(arrY[y]))
				{
					numSimilarities++;
				}
			}
		}
		return numSimilarities;
	}
	
	public static boolean checkForWordInStory(String word, String story)
	{
		if(story.contains(word))
		{
			return true;
		}
		return false;
	}
	
	public static ArrayList<String> compareStories(List<String> stories, String resource)
	{
		ArrayList<String> arrayPairs = new ArrayList<String>();
		double numSimilarities = 0.0;
		
		for(int i = 0; i < stories.size(); i++)
		{
			for(int j = 0; j < stories.size(); j++)
			{
				numSimilarities = 0.0;
				String currentLineX = stories.get(i);
				String currentLineY = stories.get(j);
				boolean wordInStoryX = checkForWordInStory(resource, currentLineX);
				boolean wordInStoryY = checkForWordInStory(resource, currentLineY);
				String[] arrX = currentLineX.split(" ");
				String[] arrY = currentLineY.split(" ");
				numSimilarities = countSimilarities(arrX, arrY, numSimilarities);
				
				if(arrX.length > arrY.length )
				{
					double jaccard = numSimilarities / arrY.length;
					if(jaccard >= .5 && i != j && (wordInStoryX == true || wordInStoryY== true))
					{
						StringBuilder sb = new StringBuilder();
						sb.append("");
						sb.append(i+1);
						sb.append(",");
						sb.append(j+1);
						String strI = sb.toString();
						arrayPairs.add(strI);
					}
				}
				else
				{
					double jaccard = numSimilarities / arrX.length;
					if(jaccard >= .5 && i != j && (wordInStoryX == true || wordInStoryY== true))
					{
						StringBuilder sb = new StringBuilder();
						sb.append("");
						sb.append(i+1);
						sb.append(",");
						sb.append(j+1);
						String strI = sb.toString();
						arrayPairs.add(strI);
					}
				}
			}
		}
		return arrayPairs;
	}
	@SuppressWarnings("null")
	public static void main(String Args[]) throws IOException
	{
		File file = new File(Args[0]);
		
		FileInputStream fis = null;
		fis = new FileInputStream(file);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
		List<String> stories = new ArrayList<String>();
		try {
		  while (true) {
		    String line = reader.readLine();
		    if (line == null) break;
		    
		    if(line.contains("that:"))
		    {
			    String[] fields = line.split("So that:");
			    // process fields her
			    stories.add(fields[1].toString());
		    }
		  }
		} finally {
		  reader.close();
		}
		String resource = Args[1];
		ArrayList<String> arrayPairs = compareStories(stories, resource);
		
		ArrayList<String> extraArrayPairs = new ArrayList<String>();
		//optional getting rid of extraneous mirror pairs
		for(int x = 0; x < arrayPairs.size(); x++)
		{
			for(int y = 0; y < arrayPairs.size(); y++)
			{
				String temp = arrayPairs.get(y);
				String[] splitter = temp.split(",");
				String newString = splitter[1].concat(",").concat(splitter[0]);
				if(arrayPairs.get(x).equals(newString) == false && x != y && !extraArrayPairs.contains(arrayPairs.get(x)))
				{
					extraArrayPairs.add(arrayPairs.get(x));
				}
				
			}
		}
//		for(int a = 0; a < arrayPairs.size(); a++)
//		{
//			for(int b = 0; b < extraArrayPairs.size()/2; b++)
//				{
//					if(arrayPairs.get(a).equals(extraArrayPairs.get(b)))
//					{
//						
//					}
//				}
//		}
//		
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		for(int i = 0; i < arrayPairs.size(); i++)
		{
			writer.println(arrayPairs.get(i));
		}
		writer.close();
		
		System.out.println(extraArrayPairs);
		System.out.println(arrayPairs);
	}
}
