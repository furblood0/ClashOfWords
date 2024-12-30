package com.furkan.clashofwords.ui.shop;

/**
 * Represents an item in the shop with its properties.
 */
public class ShopItem {
    private final int imageResId; // Resource ID of the item's icon
    private final String title; // Title of the shop item
    private final String description; // Description of the shop item
    private final int energyAmount; // Amount of energy provided by the item
    private final int goldCost; // Cost of the item in gold

    /**
     * Constructs a ShopItem.
     *
     * @param imageResId    Resource ID of the item's icon.
     * @param title         Title of the shop item.
     * @param description   Description of the shop item.
     * @param energyAmount  Amount of energy provided by the item.
     * @param goldCost      Cost of the item in gold.
     */
    public ShopItem(int imageResId, String title, String description, int energyAmount, int goldCost) {
        this.imageResId = imageResId;
        this.title = title;
        this.description = description;
        this.energyAmount = energyAmount;
        this.goldCost = goldCost;
    }

    /**
     * Gets the resource ID of the item's icon.
     *
     * @return The resource ID.
     */
    public int getImageResId() {
        return imageResId;
    }

    /**
     * Gets the title of the shop item.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the description of the shop item.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the amount of energy provided by the item.
     *
     * @return The energy amount.
     */
    public int getEnergyAmount() {
        return energyAmount;
    }

    /**
     * Gets the cost of the item in gold.
     *
     * @return The gold cost.
     */
    public int getGoldCost() {
        return goldCost;
    }
}
