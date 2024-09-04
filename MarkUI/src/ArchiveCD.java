/**
 * ArchiveCD.java
 *
 * The archive CD class for the application.
 * This class stores data for a specific entry in the application.
 *
 * Version 1.00.
 * Author: Ryhan Khan.
 */
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

    /**
     * This constructor takes in all the details required for an entry.
     *
     * @param id The number used as a unique identifier for the entry.
     * @param title The label for the CD.
     * @param author The credit/creator of the CD.
     * @param section The section for the CD.
     * @param x The x position for the CD.
     * @param y The y position for the CD.
     * @param barcode The barcode number for the CD.
     * @param description The description for the CD.
     * @param onLoan The status of whether the CD is on loan.
     */
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
