package org.wso2.carbon.ml.preprocessor;

import java.util.ArrayList;

/**
 * Created by tharik on 12/4/14.
 */
public class Customer {

    private String companyIndex;

    private int downloadActivityCount;
    private int whitePaperActivityCount;
    private int tutorialActivityCount;
    private int workshopActivityCount;
    private int caseStudiesActivityCount;
    private int productPagesActivityCount;
    private int otherActivityCount;
    private int seniorTitleCount;
    private int juniorTitleCount;

    private String companyName;
    private boolean isCustomer;
    private String joinedDate;

    private ArrayList<String> activityTimestamps;
    private ArrayList<String> countries;

    private String medianTimeBetweenTwoActivities;
    private String maxTimeBetweenTwoActivities;

    public String getCompanyIndex() {
        return companyIndex;
    }

    public void setCompanyIndex(String companyIndex) {
        this.companyIndex = companyIndex;
    }

    public int getDownloadActivityCount() {
        return downloadActivityCount;
    }

    public void setDownloadActivityCount(int downloadActivityCount) {
        this.downloadActivityCount = downloadActivityCount;
    }

    public int getWhitePaperActivityCount() {
        return whitePaperActivityCount;
    }

    public void setWhitePaperActivityCount(int whitePaperActivityCount) {
        this.whitePaperActivityCount = whitePaperActivityCount;
    }

    public int getTutorialActivityCount() {
        return tutorialActivityCount;
    }

    public void setTutorialActivityCount(int tutorialActivityCount) {
        this.tutorialActivityCount = tutorialActivityCount;
    }

    public int getWorkshopActivityCount() {
        return workshopActivityCount;
    }

    public void setWorkshopActivityCount(int workshopActivityCount) {
        this.workshopActivityCount = workshopActivityCount;
    }

    public int getCaseStudiesActivityCount() {
        return caseStudiesActivityCount;
    }

    public void setCaseStudiesActivityCount(int caseStudiesActivityCount) {
        this.caseStudiesActivityCount = caseStudiesActivityCount;
    }

    public int getProductPagesActivityCount() {
        return productPagesActivityCount;
    }

    public void setProductPagesActivityCount(int productPagesActivityCount) {
        this.productPagesActivityCount = productPagesActivityCount;
    }

    public int getOtherActivityCount() {
        return otherActivityCount;
    }

    public void setOtherActivityCount(int otherActivityCount) {
        this.otherActivityCount = otherActivityCount;
    }

    public int getSeniorTitleCount() {
        return seniorTitleCount;
    }

    public void setSeniorTitleCount(int seniorTitleCount) {
        this.seniorTitleCount = seniorTitleCount;
    }

    public int getJuniorTitleCount() {
        return juniorTitleCount;
    }

    public void setJuniorTitleCount(int juniorTitleCount) {
        this.juniorTitleCount = juniorTitleCount;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isCustomer() {
        return isCustomer;
    }

    public void setCustomer(boolean isCustomer) {
        this.isCustomer = isCustomer;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }

    public ArrayList<String> getActivityTimestamps() {
        return activityTimestamps;
    }

    public void setActivityTimestamps(ArrayList<String> activityTimestamps) {
        this.activityTimestamps = activityTimestamps;
    }

    public ArrayList<String> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<String> countries) {
        this.countries = countries;
    }

    public String getMedianTimeBetweenTwoActivities() {
        return medianTimeBetweenTwoActivities;
    }

    public void setMedianTimeBetweenTwoActivities(String medianTimeBetweenTwoActivities) {
        this.medianTimeBetweenTwoActivities = medianTimeBetweenTwoActivities;
    }

    public String getMaxTimeBetweenTwoActivities() {
        return maxTimeBetweenTwoActivities;
    }

    public void setMaxTimeBetweenTwoActivities(String maxTimeBetweenTwoActivities) {
        this.maxTimeBetweenTwoActivities = maxTimeBetweenTwoActivities;
    }
}
