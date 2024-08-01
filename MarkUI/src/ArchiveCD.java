public class ArchiveCD
{
    private String title, author, description;
    private char section;
    private int id, x, y, barcode;
    private boolean onLoan;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public char getSection() {
        return section;
    }

    public int getX() {
        return x;
    }

    public int getID() {
        return id;
    }

    public int getY() {
        return y;
    }

    public int getBarcode() {
        return barcode;
    }

    public boolean getLoan() {
        return onLoan;
    }
}
