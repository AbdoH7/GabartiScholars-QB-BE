package org.example.gabartischolarsqbbe.entity;

/**
 * Entity class representing a multiple choice question option
 */
public class McqOption {
    
    private String value;
    private Boolean correct;
    
    /**
     * Default constructor
     */
    public McqOption() {
    }
    
    /**
     * Constructor with parameters
     * @param value the option text/value
     * @param correct whether this option is correct
     */
    public McqOption(String value, Boolean correct) {
        this.value = value;
        this.correct = correct;
    }
    
    /**
     * Get the option value/text
     * @return the option value
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Set the option value/text
     * @param value the option value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    /**
     * Check if this option is correct
     * @return true if this option is correct, false otherwise
     */
    public Boolean getCorrect() {
        return correct;
    }
    
    /**
     * Set whether this option is correct
     * @param correct true if this option is correct, false otherwise
     */
    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
    
    @Override
    public String toString() {
        return "McqOption{" +
                "value='" + value + '\'' +
                ", correct=" + correct +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        McqOption mcqOption = (McqOption) obj;
        
        if (value != null ? !value.equals(mcqOption.value) : mcqOption.value != null) return false;
        return correct != null ? correct.equals(mcqOption.correct) : mcqOption.correct == null;
    }
    
    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (correct != null ? correct.hashCode() : 0);
        return result;
    }
}
