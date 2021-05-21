package de.uni_trier.wi2.pki.io.attr;

public class Continuously implements CSVAttribute{

    private Object value;
    private int category;


    public Continuously(Object value) {
        this.value = value;
    }

    @Override
    public Object getCategory() {       // int doesnt work
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public Object clone() {
        return this.clone();
    }

    @Override
    public int compareTo(Object o) {
        double ownValue = Double.parseDouble(value.toString());
        double oValue = Double.parseDouble((((Continuously) o).getValue()).toString());

        if(ownValue < oValue){
            return 1;
        }else if(ownValue > oValue){
            return -1;
        }else{
            return 0;
        }
    }

}
