import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class JavaClock {

    int hours;
    int minutes;
    int seconds;
    int milisec;

    Calendar cal1;
    Calendar cal2;

    int sec_midnight;

    int clock;


    public JavaClock() {

        cal1=new GregorianCalendar(2019, 1, 3, 12, 0, 0);


    }


    public JavaClock(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public JavaClock(int clock) {
        this.clock = clock;
        cal1.add(Calendar.SECOND,clock);
    }


    public void setClock(int clock) {
        this.clock = clock;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void tick(){



        milisec=60*1000;

        for(int i=1;i<10;i++){

            cal1.add(Calendar.MILLISECOND,milisec);

            System.out.println("Time "+i+": "+cal1.getTime());

        }




    }



    public JavaClock addClock(JavaClock fisrtClock,JavaClock secondClock){

        int firstClock_hr=fisrtClock.getHours();
        int firstClock_min=fisrtClock.getMinutes();
        int firstClock_sec=fisrtClock.getSeconds();


        int secondClock_hr=secondClock.getHours();
        int secondClock_min=secondClock.getMinutes();
        int secondClock_sec=secondClock.getSeconds();

        //adding the number of hours, minutes and seconds of both the first and second clock
        int hrs=firstClock_hr+secondClock_hr;
        int min=firstClock_min+secondClock_min;
        int secs=firstClock_sec+secondClock_sec;


        cal1=new GregorianCalendar(2019, 1, 3, hrs, min, secs);

        Date time=cal1.getTime();// sum of both first clock and second clock

        toString(time,fisrtClock,secondClock);//print the clock values


        return null;
    }



    public String toString(Date time, JavaClock firstClock,JavaClock secondClock) {

        cal1=new GregorianCalendar(2019, 1, 3, firstClock.getHours(), firstClock.getMinutes(), firstClock.getSeconds());

        cal2=new GregorianCalendar(2019, 1, 3, secondClock.getHours(), secondClock.getMinutes(), secondClock.getSeconds());



        return "First Clock="+cal1.getTime()+"\n"+"Second Clock="+cal1.getTime()+"\n\n"+"Sum of two Clocks="+cal1.getTime()+"\n";
    }

/*
    public void tickDown(){

        milisec=cal1.getTimeInMillis();

        milisec=milisec-60*1000;

    }*/



}
