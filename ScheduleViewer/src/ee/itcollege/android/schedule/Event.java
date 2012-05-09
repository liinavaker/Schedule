package ee.itcollege.android.schedule;


/**
 * @author Liina
 *
 */
public class Event {

    private String weekday;
    private String startDate;
    private String endDate;
    private String subject; // aine-loeng
    private String location;
    private String subjectID; // ainekood
    private String lecturer;
    private String subjectType; // aine t��p: loeng/praktikum/eksam jne.

    public String getWeekday() {
	return this.weekday;
    }

    public void setWeekday(String weekday) {
	this.weekday = weekday;
    }

    public String getStartDate() {
	return this.startDate;
    }

    public void setStartDate(String startDate) {
	this.startDate = startDate;
    }

    public String getEndDate() {
	return this.endDate;
    }

    public void setEndDate(String endDate) {
	this.endDate = endDate;
    }

    public String getSubject() {
	return this.subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public String getLocation() {
	return this.location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    public String getSubjectID() {
	return this.subjectID;
    }

    public void setSubjectID(String subjectID) {
	this.subjectID = subjectID;
    }

    public String getLecturer() {
	return this.lecturer;
    }

    public void setLecturer(String lecturer) {
	this.lecturer = lecturer;
    }

    public String getSubjectType() {
	return this.subjectType;

    }
}
