package spell;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {

	private Trie trie_;
	Scanner inFile = null;
	Set<String> alterationSet;
	Map<String, Integer> possibleWordMap;
	Map<String, Integer> suggestedWord;

	//Constructor
	public SpellCorrector() {
		trie_ = new Trie();
		alterationSet = new HashSet<String>();
		possibleWordMap = new TreeMap<String, Integer>();
		suggestedWord = new TreeMap<String, Integer>();
	}

	/**
	 * Tells this <code>SpellCorrector</code> to use the given file as its dictionary
	 * for generating suggestions.
	 * @param dictionaryFileName File containing the words to be used
	 * @throws IOException If the file cannot be read
	 */
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		inFile = new Scanner(new File(dictionaryFileName));
		while (inFile.hasNextLine()) {
			trie_.add(inFile.nextLine());
		}

		//TEST
		//System.out.println("Test7");
		//System.out.print(trie_.toString());
		//System.out.println("Nodes = " + trie_.nodeCount + " Words = " +
		//		trie_.wordCount);
	}

	/**
	 * Suggest a word from the dictionary that most closely matches
	 * <code>inputWord</code>
	 * @param inputWord
	 * @return The suggestion or null if there is no similar word in the dictionary
	 */
	@Override
	public String suggestSimilarWord(String inputWord) {
		alterationSet.clear();
		possibleWordMap.clear();
		suggestedWord.clear();

		if (trie_.find(inputWord) == null) {


			//Code to find most similar word
			callFourEditDistances(inputWord);
			addEditedWordsToNewMap();
			String similarWord = findWordWithHighestFrequency();

			//If there are no suggestions, perform an edit distance of two
			if (similarWord == null)
				similarWord = editDistanceTwo();

			//Return the result.  If there were no suggestions, it will return NULL
			return similarWord;
		}
		else
			return inputWord.toLowerCase();



	}

	public String editDistanceTwo() {
		Set<String> newAlterationSet = new HashSet<String>(alterationSet);
		for (String s : newAlterationSet)
			callFourEditDistances(s);
		addEditedWordsToNewMap();

		return findWordWithHighestFrequency();
	}

	public String findWordWithHighestFrequency() {
		int highest = 0;
		if (possibleWordMap.size() > 0) {
			for (Map.Entry<String, Integer> entry : possibleWordMap.entrySet()) {
				if (entry.getValue() > highest)
					highest = entry.getValue();
			}
			for (Map.Entry<String, Integer> entry : possibleWordMap.entrySet()) {
				if (highest == entry.getValue()) {
					suggestedWord.put(entry.getKey(), entry.getValue());
					//TEST
					//System.out.println("Putting: " + entry.getKey());
				}
			}

			Map.Entry<String, Integer> entry = suggestedWord.entrySet().iterator().next();
			return entry.getKey();
		}

		//If no words exist, then output null
		else
			return null;
	}

	public void addEditedWordsToNewMap() {
		for (String s : alterationSet) {
			Node n = (Node)trie_.find(s);
			if (n != null) {
				possibleWordMap.put(s,n.getValue());
				//TEST
				//System.out.println(s + " added to possibleWordSet\n" +
				//		s + " has frequency of " + n.getValue());
			}
		}
	}

	private void callFourEditDistances(String inputWord) {
		deletionDistance(inputWord);
		transpositionDistance(inputWord);
		alterationDistance(inputWord);
		insertionDistance(inputWord);
	}

	public void deletionDistance(String inputWord) {
		StringBuilder sb = new StringBuilder();

		//Go through each element in the string and delete them individually
		//and add them to the set
		for (int i = 0; i < inputWord.length(); i++) {
			sb.append(inputWord);
			sb.deleteCharAt(i);
			alterationSet.add(sb.toString());
			//TEST
			//System.out.println(sb.toString());
			sb.delete(0, sb.length());
		}
	}

	//Swap each adjacent letter and add to the set
	public void transpositionDistance(String inputWord) {
		char[] c = inputWord.toCharArray();
		char temp;

		for (int i = 0; i < inputWord.length()-1; i++) {
			temp = c[i];
			c[i] = c[i+1];
			c[i+1] = temp;
			alterationSet.add(String.valueOf(c));
			//TEST
			//System.out.println(String.valueOf(c));
			c = inputWord.toCharArray();
		}
	}

	//Change each letter of the inputWord to every letter in the alphabet
	public void alterationDistance(String inputWord) {
		char[] c;

		for (int index = 0; index < inputWord.length(); index++) {
			c = inputWord.toCharArray();

			for (char letter = 'a'; letter <= 'z'; letter++) {
				c[index] = letter;
				alterationSet.add((String.valueOf(c)));
				//TEST
				//System.out.println(String.valueOf(c));
			}
		}
	}

	//Add an extra letter of the alphabet in each index of the inputWord
	public void insertionDistance(String inputWord) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i <= inputWord.length(); i++) {

			for (char letter = 'a'; letter <= 'z'; letter++) {
				sb.append(inputWord);
				sb.insert(i, letter);
				alterationSet.add(sb.toString());
				//TEST
				//System.out.println(sb.toString());
				sb.delete(0, sb.length());
			}
		}
	} //End of insertionDistance() method

} //End of SpellCorrector class
