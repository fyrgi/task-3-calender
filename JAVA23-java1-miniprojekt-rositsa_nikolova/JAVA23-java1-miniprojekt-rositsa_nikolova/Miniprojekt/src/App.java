import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;

/*TODO 1. Da opravq viziqta na:
       - dailyLabel - da se pokazva celiq label nezavisimo kolko dylyg e.
       - AddButton
      2. Da izkarvam poveche ot edin note. Veche gi pazq v MapOfNotes DA
       - kak da resha problema ako ima prekaleno mnogo notisi? Scroll?
      4. Da si podredq koda. Tova nqma kak da stane predi polunosht.
*/
public class App extends JFrame implements ActionListener {

    private JPanel dayPanel, dateDisplayPanel, addNotePanel, contentPanel;
    private JButton buttonAdd, prevWeek, nextWeek;
    private JLabel weekDayLabel, dateLabel, dailyNote;
    private ArrayList<JTextField> noteField = new ArrayList<JTextField>();
    private JTextField note;
    private ArrayList<JLabel> dailyNotes = new ArrayList<JLabel>();


    // Create an ArrayList of type LocalDateTime where the information
    // of the current week's days is stored. Starting from Monday.
    private ArrayList<LocalDateTime> allWeekDays = Date.currentWeek();
    private HashMap<String, ArrayList<String>> allNotes = new HashMap<String, ArrayList<String>>();
    private Border dayBorder = BorderFactory.createRaisedBevelBorder();



    // Iteration. Right now is hardcoded
    private int weekLength = 0;
    boolean isFirst= true;

    Color higlightBg = new Color(163, 240, 181);
    Color higlightFn = new Color(10, 72, 10);


    App(){
        this.setSize(980,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 9, 5, 0));
        //Create a panel for each day of the week.
        displayWeek();
        this.setVisible(true);
        ImageIcon icon = new ImageIcon("vecteezy_flat-icon-calendar-month-png-illustration-color-banner_21049127.png");
        this.setIconImage(icon.getImage()); // does not work. Not a problem
    }

    // This is the constructor of my app!. Everything that is into the frames.
    // Every logical piece of code could have been in their own function for easier reading.
    // Currently the the view and the controller are on the same class.
    private void displayWeek(){
        prevWeek = new JButton("<<");
        prevWeek.setToolTipText("Go to previous week");
        prevWeek.setActionCommand("prev");
        prevWeek.addActionListener(this);
        this.add(prevWeek);

        for(LocalDateTime weekDay: allWeekDays){
            this.setTitle("Weekly calender. Today is: " + Date.getDate(Date.today));
            String code = ""+weekDay.getYear()+weekDay.getMonth().getValue()+weekDay.getDayOfMonth();
            dayPanel = new JPanel();
            dayPanel.setLayout(new BorderLayout());
            dayPanel.setName(code);
            dayPanel.setBorder(dayBorder);

// -----------------------------------------------------------------------------------------------------------
            // Display Date and day. Set some font features
            dateDisplayPanel = new JPanel();
            dateDisplayPanel.setLayout(new GridLayout(2,1));
            dateDisplayPanel.setSize(100, 50);
            dateDisplayPanel.setBorder(BorderFactory.createMatteBorder(
                    0, 0, 3, 0, Color.black));
            dayPanel.add(dateDisplayPanel,BorderLayout.NORTH);
            dateLabel = new JLabel(Date.getDate(weekDay));
            dateDisplayPanel.add(dateLabel);
            weekDayLabel = new JLabel(weekDay.getDayOfWeek().toString().toUpperCase());
            dateDisplayPanel.add(weekDayLabel);
            dateLabel.setPreferredSize(new Dimension(100, 25));
            dateLabel.setFont(new Font("Times new roman", Font.BOLD, 16));
            dateLabel.setHorizontalAlignment(JLabel.CENTER);
            dateLabel.setVerticalAlignment(JLabel.CENTER);
            weekDayLabel.setPreferredSize(new Dimension(100, 25));
            weekDayLabel.setFont(new Font("Times new roman", Font.BOLD, 16));
            weekDayLabel.setHorizontalAlignment(JLabel.CENTER);
            weekDayLabel.setVerticalAlignment(JLabel.CENTER);

// -----------------------------------------------------------------------------------------------------------
            contentPanel = new JPanel();
            contentPanel.setLayout(new FlowLayout());
            dayPanel.add(contentPanel,BorderLayout.CENTER);

            dailyNotes.add(new JLabel());
            dailyNote = dailyNotes.get(weekLength);
            dailyNote.setBounds(0,0,140,400);
            dailyNote.setName(code);
            if(!allNotes.isEmpty()){
                displayNotes(dailyNote.getName());
            }
            contentPanel.add(dailyNote);

// -----------------------------------------------------------------------------------------------------------
            //addnotepanel is going to be added as one at the bottom of the calendar
            addNotePanel = new JPanel();
            addNotePanel.setLayout(new GridLayout(2,1));
            addNotePanel.setSize(100, 50);
            dayPanel.add(addNotePanel,BorderLayout.SOUTH);
            // each display holds its own instance of Textfield for a note.
            // I need them to act as a separate unit, that's why
            // I store them in an ArrayList of objects of type JTextField
            noteField.add(new JTextField(20));
            note = noteField.get(weekLength);
            //note.setPreferredSize(new Dimension(100, 50));
            note.setBounds(80,30,120,40);
            note.setText("");
            note.setName(code);
            note.setActionCommand(code);
            note.addActionListener(this);

            buttonAdd = new JButton("ADD");
            buttonAdd.setActionCommand(code);
            buttonAdd.addActionListener(this);


            addNotePanel.add(noteField.get(weekLength), BorderLayout.NORTH);
            addNotePanel.add(buttonAdd, BorderLayout.SOUTH);
// -----------------------------------------------------------------------------------------------------------
            this.add(dayPanel);
            weekLength++;

            // add some color to the current day
            if(weekDay.equals(Date.today)){
                dateDisplayPanel.setBorder(BorderFactory.createMatteBorder(
                        0, 0, 3, 0, higlightFn));
                dateDisplayPanel.setBackground(higlightBg);
                contentPanel.setBackground(higlightBg);
                weekDayLabel.setForeground(higlightFn);
                dateLabel.setForeground(higlightFn);
            }
        }
// -----------------------------------------------------------------------------------------------------------
        nextWeek = new JButton(">>");
        nextWeek.setToolTipText("Go to next week");
        nextWeek.setActionCommand("next");
        nextWeek.addActionListener(this);
        this.add(nextWeek);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String currentID = e.getActionCommand();
        //check if there is something in the textfield. Don't add a string of whitespaces. Remove leading and ending whitespaces.
        if(currentID.equals("prev") || currentID.equals("next")){
            isFirst = false;
            allWeekDays = Date.displayedWeek(currentID, isFirst);
            getContentPane().removeAll();
            displayWeek();
            validate();
            repaint();

        } else {
            addNewNote(currentID);
        }
    }

    // Function to add a new record. A bit redundant with displayNotes()
    private void addNewNote(String currentID){
        String notes;

        for (JTextField note : noteField) {
            if (!note.getText().trim().equals("")) {
                if (note.getName().equals(currentID)) {
                    notes = note.getText().trim();
                    MapOfNotes.addNotes(note.getName(), notes);
                    allNotes = MapOfNotes.getAllNotes();
                    for (JLabel record : dailyNotes) {
                        if (record.getName().equals(currentID)) {
                            String dailyRecord = "";   // It needs a value for the first record.
                            for (String dayRecord : allNotes.get(currentID)) {
                                dailyRecord = "" + dailyRecord + "<br/>" + dayRecord;
                            }
                            record.setText("<HTML>" + dailyRecord + "</HTML>");
                        }
                    }
                    note.setText("");
                }
            }
        }
    }


    //This function is used when switching between screens to rewrite the notes that already are there.
    //TODO fix a bit of the interaction with other components

    private void displayNotes(String code){
        ArrayList<String> notesForDay= new ArrayList<String>();
        String dailyRecord = "";
        for (JLabel record : dailyNotes) {
            if(record.getName().equals(code)&&record.getText().isEmpty()){
                for(String key : allNotes.keySet()){
                    if(record.getName().equals(key)) {
                        notesForDay = allNotes.get(key);
                    }
                }
                for(String note: notesForDay){
                    dailyRecord = "" + note + "<br/>" + dailyRecord;
                }
                record.setText("<HTML>" + dailyRecord + "</HTML>");
            }
        }
    }
}