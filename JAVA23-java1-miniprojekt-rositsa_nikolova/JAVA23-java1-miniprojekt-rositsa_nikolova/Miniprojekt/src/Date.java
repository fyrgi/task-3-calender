import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class Date {
    static LocalDateTime today = LocalDateTime.now();
    static ArrayList<LocalDateTime> landingWeek = currentWeek();


    // The getDate will simply take the current date from the GUI and return it formatted
    public static String getDate(LocalDateTime day){
        DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy\n");
        return day.format(dayFormat);
    }

    // This function helps me find the first day of the current week (for the project is Monday).
    public static LocalDateTime findMonday(LocalDateTime date){
        LocalDateTime tempDate = date;
        String dayOfWeek = String.valueOf(tempDate.getDayOfWeek());
        boolean isMonday = false;
        if(Objects.equals(dayOfWeek, "MONDAY")){
            isMonday = true;
        }
        while(!isMonday){
            tempDate = tempDate.minusDays(1);
            // this syntax was suggested by the IDE. I had no idea it has to be written that way to assign the string to a string
            dayOfWeek = String.valueOf(tempDate.getDayOfWeek());
            if(Objects.equals(dayOfWeek, "MONDAY")){
                isMonday = true;
            }
        };
        return tempDate;
    }

    //Finds all days of the current week starting from Monday. We find Monday in the previous function findMonday()
    public static ArrayList<LocalDateTime> currentWeek(){
        LocalDateTime monday = findMonday(today);
        ArrayList<LocalDateTime> currentWeek = new ArrayList<>();
        for(int i = 0; i <7; i++){
            currentWeek.add(monday.plusDays(i));
        }
        return currentWeek;
    }

    // Switch between weeks. Hard coded length of week. Can be better!
    public static ArrayList<LocalDateTime> displayedWeek(String direction, boolean isFirst){

        if(isFirst){
            LocalDateTime newMonday=currentWeek().get(0);
            if(direction.equals("next")){
                newMonday = newMonday.plusWeeks(1);
            } else {
                newMonday = newMonday.minusWeeks(1);
            }
            landingWeek.clear();
            for(int i = 0; i <7; i++){
                landingWeek.add(newMonday.plusDays(i));
            }
        } else {
            LocalDateTime newMonday=landingWeek.get(0);
            if(direction.equals("next")){
                newMonday=landingWeek.get(0).plusWeeks(1);

            } else {
                newMonday = landingWeek.get(0).minusWeeks(1);
            }
            landingWeek.clear();
            for(int i = 0; i <7; i++){
                landingWeek.add(newMonday.plusDays(i));
            }
        }
        return landingWeek;
    }
}