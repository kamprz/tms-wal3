package wat.semestr7.bachelor.exception;

public class JsonParsingException extends Exception
{
    public JsonParsingException(Exception e)
    {
        super(e);
    }
    public JsonParsingException(String s)
    {
        super(s);
    }
}
