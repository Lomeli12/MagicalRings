package net.lomeli.ring.api.interfaces;

/**
 * Use for blocks and entities to get more info about them in the ring book
 *
 * @author Lomeli12
 */
public interface IBookEntry {
    /**
     * Get the page that the book should look for
     *
     * @param metadata - metadata of block or item. Entities that don't extend EntityItem will use getData instead.
     * @return
     */
    public String getBookPage(int metadata);

    /**
     * Used by entities that don't extend EntityItem to use in lue of metadata
     *
     * @return
     */
    public int getData();
}
