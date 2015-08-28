import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

public class MP1 {
	Random generator;
	String userName;
	String inputFileName;
	String delimiters = " \t,;.?!-:@[](){}_*/";
	String[] stopWordsArray = { "i", "me", "my", "myself", "we", "our", "ours",
			"ourselves", "you", "your", "yours", "yourself", "yourselves",
			"he", "him", "his", "himself", "she", "her", "hers", "herself",
			"it", "its", "itself", "they", "them", "their", "theirs",
			"themselves", "what", "which", "who", "whom", "this", "that",
			"these", "those", "am", "is", "are", "was", "were", "be", "been",
			"being", "have", "has", "had", "having", "do", "does", "did",
			"doing", "a", "an", "the", "and", "but", "if", "or", "because",
			"as", "until", "while", "of", "at", "by", "for", "with", "about",
			"against", "between", "into", "through", "during", "before",
			"after", "above", "below", "to", "from", "up", "down", "in", "out",
			"on", "off", "over", "under", "again", "further", "then", "once",
			"here", "there", "when", "where", "why", "how", "all", "any",
			"both", "each", "few", "more", "most", "other", "some", "such",
			"no", "nor", "not", "only", "own", "same", "so", "than", "too",
			"very", "s", "t", "can", "will", "just", "don", "should", "now" };

	void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA");
		messageDigest.update(seed.toLowerCase().trim().getBytes());
		byte[] seedMD5 = messageDigest.digest();

		long longSeed = 0;
		for (int i = 0; i < seedMD5.length; i++) {
			longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
		}

		this.generator = new Random(longSeed);
	}

	Integer[] getIndexes() throws NoSuchAlgorithmException {
		Integer n = 10000;
		Integer number_of_lines = 50000;
		Integer[] ret = new Integer[n];
		this.initialRandomGenerator(this.userName);
		for (int i = 0; i < n; i++) {
			ret[i] = generator.nextInt(number_of_lines);
		}
		return ret;
	}

	public MP1(String userName, String inputFileName) {
		this.userName = userName;
		this.inputFileName = inputFileName;
	}

	public String[] process() throws Exception {
		String[] ret = new String[20];
		
        Map<String, Integer> map = new HashMap<String, Integer>();
        Map<Integer, String> linesMap = new HashMap<Integer, String>();
        Integer idx = 0;
        List<String> stopWordsList = Arrays.asList(stopWordsArray);
        

        List<Integer> indexes = Arrays.asList(getIndexes());
        List<String> lines = Files.readAllLines(FileSystems.getDefault().getPath(this.inputFileName), StandardCharsets.UTF_8);
        for (String line : lines) {
            linesMap.put(idx, line);
            idx++;
        }

        for (Integer index : indexes){
            String line = linesMap.get(index);
            StringTokenizer st = new StringTokenizer(line, delimiters);
            while (st.hasMoreElements()) {
                String word = st.nextToken().toLowerCase().trim();
                if (word.isEmpty() || stopWordsList.contains(word)) {
                    continue;
                }
                if (map.containsKey(word)) {
                    Integer count = map.get(word);
                    count++;
                    map.put(word, count);
                } else {
                    map.put(word, 1);
                }
            }
        }
		
		
		
		
		
		
		
		
		// INFO3 sort lexicographic (also known as dictionary order )
		Map<String, Integer> map22 = new TreeMap<String, Integer>(map);

		// info 4 now sort on number of counts (ie hashmap value)
		Set<Entry<String, Integer>> set = map22.entrySet();
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(
				set);

		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		List<String> where = new ArrayList<String>();
		Integer counter = 0;

		// info 5 top 20
		for (Map.Entry<String, Integer> entry : list) {
			if (counter < 20) {

				where.add(entry.getKey());
//				System.out.println(entry.getKey()+"<><>"+map.get(entry.getKey()));
			}
			counter++;

		}
//	 System.exit(0);
		where.toArray(ret);
		return ret;
	}

	public LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
		List mapKeys = new ArrayList(passedMap.keySet());
		List mapValues = new ArrayList(passedMap.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);

		LinkedHashMap sortedMap = new LinkedHashMap();

		Iterator valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				String comp1 = passedMap.get(key).toString();
				String comp2 = val.toString();

				if (comp1.equals(comp2)) {
					passedMap.remove(key);
					mapKeys.remove(key);
					sortedMap.put((String) key, (Double) val);
					break;
				}

			}

		}
		return sortedMap;
	}

	static <K, V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(
			Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(
				map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		});

		return sortedEntries;
	}

	 public static void main(String[] args) throws Exception {
	        if (args.length < 1){
	            System.out.println("MP1 <User ID>");
	        }
	        else {
	            String userName =args[0];
	            String inputFileName = "./input.txt";
	            MP1 mp = new MP1(userName, inputFileName);
	            String[] topItems = mp.process();
	            for (String item: topItems){
	                System.out.println(item);
	            }
	        }
	    }
	}
