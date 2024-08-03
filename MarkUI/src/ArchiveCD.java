public class ArchiveCD
{
    private String mTitle,mAuthor, mDescription;
    private char mSection;
    private int mID, mX, mY, mBarcode;
    private boolean mOnLoan;

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDescription() {
        return mDescription;
    }

    public char getSection() {
        return mSection;
    }

    public int getX() {
        return mX;
    }

    public int getID() {
        return mID;
    }

    public int getY() {
        return mY;
    }

    public int getBarcode() { return mBarcode; }

    public boolean getLoan() {
        return mOnLoan;
    }

    public ArchiveCD() {

    }

    public ArchiveCD(int id, String title, String author, char section, int x, int y, int barcode, String description, boolean onLoan) {
        mID = id;
        mTitle = title;
        mAuthor = author;
        mSection = section;
        mX = x;
        mY = y;
        mBarcode = barcode;
        mDescription = description;
        mOnLoan = onLoan;
    }
}
