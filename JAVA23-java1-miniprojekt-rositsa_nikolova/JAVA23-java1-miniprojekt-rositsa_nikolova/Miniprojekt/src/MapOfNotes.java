import java.util.ArrayList;
import java.util.HashMap;

// Classen ska handla dem korta notiser vår har användare skappad. I en hashmap behålls dagenskod och en ArrayList med alla notiser som finns för dagen.
public class MapOfNotes {

    private static HashMap<String, ArrayList<String>> allNotes = new HashMap<>();

    public static HashMap<String, ArrayList<String>> addNotes(String code, String note){
        // I was trying with dailyNotes.clear(). I was just thinking if I should have an ArrayList of object when I found the obvious answer here:
        // https://stackoverflow.com/questions/20795411/java-how-to-map-multiple-arraylists-in-a-hashmap-using-a-loop
        ArrayList<String> dailyNotes = new ArrayList<String>();
        if(allNotes.containsKey(code)){
            for (String key : allNotes.keySet()){
                if(key.equals(code)){
                    dailyNotes = readDay(code);
                    dailyNotes.add(note);
                    allNotes.put(code, dailyNotes);
                }
            }
        } else {
            dailyNotes.add(note);
            allNotes.put(code, dailyNotes);
        }
        return allNotes;
    }

    public static ArrayList<String> readDay(String code){
        ArrayList<String> readDay = new ArrayList<String>();
        for (String key : allNotes.keySet()){
            if(key.equals(code)){
                readDay = allNotes.get(key);
            }
        }
        return readDay;
    }

    public static HashMap<String, ArrayList<String>> getAllNotes() {
        return allNotes;
    }
}
