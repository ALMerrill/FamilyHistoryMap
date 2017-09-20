package access;
import java.util.Random;

public class IDao {

    public IDao(){}

    public static class DatabaseException extends Exception {}

    //eventID length = 11
    //AuthToken length = 10
    //PersonID length = 9
    public static String generateString(int length)
    {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random rng = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

}
