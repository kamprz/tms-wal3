package wat.semestr7.bachelor.exception;

public class CrawlingException extends Exception
{
    public CrawlingException (Exception e)
    {
        super(e);
    }
    public CrawlingException(String s)
    {
        super(s);
    }
}