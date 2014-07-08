
public class MCTimerNano{
    private long tim;
    public MCTimerNano(){
        tim = System.nanoTime();
    }
    public double get(){
        long x = System.nanoTime() - tim;
        tim = System.nanoTime();
        return x/1000.;
    }
}