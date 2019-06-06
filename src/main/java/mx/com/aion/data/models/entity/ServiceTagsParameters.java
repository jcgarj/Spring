package mx.com.aion.data.models.entity;

public class ServiceTagsParameters {

    private String  tagNameId;
    private int     minLength;
    private int     maxLength;
    private String  tagTypeDesc;
    private boolean obligatory;
    private int     dayLenght;
    private int     monthLenght;
    private int     yearLenght;
    private int     amountIntLenght;
    private int     amountDecLenght;
    private String  split;
    private String  format;
    private String  tagValues;
    private String  depStructure;
    private String vcRuleGroup;

    public String getTagNameId() {
        return tagNameId;
    }

    public void setTagNameId(String tagNameId) {
        this.tagNameId = tagNameId;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getTagTypeDesc() {
        return tagTypeDesc;
    }

    public void setTagTypeDesc(String tagTypeDesc) {
        this.tagTypeDesc = tagTypeDesc;
    }

    public boolean isObligatory() {
        return obligatory;
    }

    public void setObligatory(boolean obligatory) {
        this.obligatory = obligatory;
    }

    public int getDayLenght() {
        return dayLenght;
    }

    public void setDayLenght(int dayLenght) {
        this.dayLenght = dayLenght;
    }

    public int getMonthLenght() {
        return monthLenght;
    }

    public void setMonthLenght(int monthLenght) {
        this.monthLenght = monthLenght;
    }

    public int getYearLenght() {
        return yearLenght;
    }

    public void setYearLenght(int yearLenght) {
        this.yearLenght = yearLenght;
    }

    public int getAmountIntLenght() {
        return amountIntLenght;
    }

    public void setAmountIntLenght(int amountIntLenght) {
        this.amountIntLenght = amountIntLenght;
    }

    public int getAmountDecLenght() {
        return amountDecLenght;
    }

    public void setAmountDecLenght(int amountDecLenght) {
        this.amountDecLenght = amountDecLenght;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTagValues() {
        return tagValues;
    }

    public void setTagValues(String tagValues) {
        this.tagValues = tagValues;
    }

    public String getDepStructure() {
        return depStructure;
    }

    public void setDepStructure(String depStructure) {
        this.depStructure = depStructure;
    }

    public String getVcRuleGroup() {
        return vcRuleGroup;
    }

    public void setVcRuleGroup(String vcRuleGroup) {
        this.vcRuleGroup = vcRuleGroup;
    }
}
