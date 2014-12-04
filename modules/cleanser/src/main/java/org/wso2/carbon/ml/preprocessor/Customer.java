package org.wso2.carbon.ml.preprocessor;

import java.util.*;
import java.util.concurrent.TimeUnit;

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

    private ArrayList<Date> activityTimeStamps;
    private ArrayList<String> countries;


    public Customer()
    {
        activityTimeStamps = new ArrayList<Date>();
        countries = new ArrayList<String>();
    }

    public void addCountry(String countryName)
    {
        countries.add(countryName);
    }

    public void addActivityTimeStamp(Date timeStamp)
    {
        activityTimeStamps.add(timeStamp);
        Collections.sort(activityTimeStamps);
    }

    public long getMedianTimeBetweenTwoActivities()
    {
        long median;
        long [] timeIntervals = this.constructIntervals();

        if (timeIntervals.length % 2 == 0) {
            median = (timeIntervals[timeIntervals.length / 2] + timeIntervals[timeIntervals.length / 2 - 1]) / 2;
        }
        else {
            median = timeIntervals[timeIntervals.length / 2];
        }

        return  median;
    }

    public long getMaxTimeBetweenTwoActivities()
    {
        long [] timeIntervals = this.constructIntervals();

        //Return last element which is the highest
        return timeIntervals[timeIntervals.length -1];

    }

    /**
     * Construct Intervals based on activity time stamps
     * @return Interval array
     */
    private long[] constructIntervals()
    {
        long [] timeIntervals = new long[activityTimeStamps.size() -1];

        for (int i = 0; i < timeIntervals.length; i++)
        {
            timeIntervals[i] = getDateDiff(activityTimeStamps.get(i), activityTimeStamps.get(i + 1), TimeUnit.MILLISECONDS);
        }

        Arrays.sort(timeIntervals);

        return  timeIntervals;

    }

    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }



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

    public ArrayList<Date> getActivityTimeStamps() {
        return activityTimeStamps;
    }

    public void setActivityTimeStamps(ArrayList<Date> activityTimestamps) {
        this.activityTimeStamps = activityTimestamps;
    }

    public ArrayList<String> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<String> countries) {
        this.countries = countries;
    }
}
