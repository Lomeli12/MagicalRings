package net.lomeli.ring.api;

/**
 * Use for blocks to get more info about them in the ring book
 * @author Lomeli12
 *
 */
public interface IBookEntry {
    /**
     * Get the page that the book should look for
     * @param metadata
     * @return
     */
    public String getBookPage(int metadata);
}
