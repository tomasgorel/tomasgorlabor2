package com.example.rename;

public class Drinks {
    private String nameId;
    private String name;
    private String category;
    private String glass;
    private String instructions;

    public Drinks(String nameId, String name, String category, String glass, String instructions) {
        this.nameId = nameId;
        this.name = name;
        this.category = category;
        this.glass = glass;
        this.instructions = instructions;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGlass() {
        return glass;
    }

    public void setGlass(String glass) {
        this.glass = glass;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
