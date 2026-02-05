package ca.pandaaa.animalquest.player.mana;

public class Mana {
    private double currentMana;
    private double maximumMana;
    private Runnable onChange;

    public void setOnChange(Runnable onChange) {
        this.onChange = onChange;
    }

    public Mana() {
        this.currentMana = 50.0;
        this.maximumMana = 50.0;
    }

    public Mana(double currentMana) {
        this.currentMana = currentMana;
        this.maximumMana = 50.0;
    }

    public double getCurrentMana() {
        return currentMana;
    }

    public double getMaximumMana() {
        return maximumMana;
    }

    public void setCurrentMana(double amount) {
        this.currentMana = Math.min(maximumMana, Math.max(0, amount));
        callOnChange();
    }

    public void setMaximumMana(double amount) {
        this.maximumMana = Math.max(1, amount);
        this.currentMana = Math.min(currentMana, maximumMana);
        callOnChange();
    }

    public void addMana(double amount) {
        setCurrentMana(currentMana + amount);
    }

    public boolean consumeMana(double amount) {
        if (currentMana >= amount) {
            setCurrentMana(currentMana - amount);
            return true;
        }
        return false;
    }

    private void callOnChange() {
        if (onChange != null)
            onChange.run();
    }

}
