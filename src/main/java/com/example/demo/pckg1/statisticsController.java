package com.example.demo.pckg1;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class statisticsController {
	
	@Autowired
	private KidRepository kidRepo;
	@Autowired
	private Parent_repository parentRepo;
	@Autowired
	private CategoryRepository categoryRepo;
	@Autowired
	private CourseRepository courseRepo;
	@Autowired
	private MeetingRepository meetingRepo;
	
	@PostMapping("/initializeinput")
	public void initializeInput() {
		String[] names = {"reem","mohammed","avital","maria","ofra","rili"}; // random names array
		String[] categories = {"space","art","music","science","poetry"}; // random categories array
		Day[] days_arr = Day.values(); // days enum array
		Gender[] gender_arr = Gender.values(); // gender enum array
		boolean[] meeting_status = {false,true}; // meeting status array
		String random_name; 
		Date random_date = new Date();
		Parent new_parent;
		int phone_num_rand;
		Kid new_kid;
		long DAY_IN_MS = 1000 * 60 * 60 * 24;
		List<Course> allCourses = new ArrayList<Course>(); // list of all courses
		List<Meeting> allMeetings = new ArrayList<Meeting>(); // list of all meetings
		
		// ---------------- create new categories and courses ----------------

		for(String categ: categories) {
			categoryRepo.addANewCategory(new Category(categ,"")); // create new category
			for(int i=0;i<8;i++) {
				Course new_course = courseRepo.addNewCourse(new Course(categ+String.valueOf(i),random_date,random_date,days_arr[new Random().nextInt(days_arr.length)],categ)); // create new course
				random_date = new Date((new Date()).getTime()- ((int)Math.floor(Math.random()*(7)+1))*DAY_IN_MS); // generate random time for meeting by week
				if(i<3) {
					random_date = new Date((new Date()).getTime()- ((int)Math.floor(Math.random()*(35)+1))*DAY_IN_MS); // generate random time for meeting by month
				}
				else if(i>=3 && i<7) {
					random_date = new Date((new Date()).getTime()- ((int)Math.floor(Math.random()*(365)+1))*DAY_IN_MS); // generate random time for meeting by year
				}
				meetingRepo.addNewMeeting(new Meeting(new_course.getID(),random_date)); // create new meeting
			}
		}
		
		allMeetings = meetingRepo.getAllMeetings(); // get all meetings
		for(Meeting meet: allMeetings) {
			meetingRepo.setStatus(meet.getId(),  meeting_status[new Random().nextInt(meeting_status.length)]); // generate random status for meeting
		}
		
		allCourses = courseRepo.getAllCourses(); // get all courses
		
		for(int i=0;i<50;i++) {
			// ---------------- add new parent ----------------
			random_name = names[new Random().nextInt(names.length)]; // generate random parent's name
			phone_num_rand = (int)(Math.random() * 1000000000) + 1000000000; // generate random parent's phone number
			new_parent = parentRepo.addNewParent(new Parent(random_name,String.valueOf(phone_num_rand),"","")); // add new parent to parent repository
						
			if(i<15) {
				random_date = new Date((new Date()).getTime()- ((int)Math.floor(Math.random()*(2*365)+1))*DAY_IN_MS); // generate random time for meeting by year
			}
			else if(i>=15 & i<25) {
				random_date = new Date((new Date()).getTime()- ((int)Math.floor(Math.random()*(35)+1))*DAY_IN_MS); // generate random time for meeting by month
			}
			else if(i>=25 & i<40) {
				random_date = new Date((new Date()).getTime()- ((int)Math.floor(Math.random()*(7)+1))*DAY_IN_MS); // generate random time for meeting by week
			}
						
			parentRepo.setActiveDate(new_parent.getId(),random_date); // change parent active date to random date			
		}
		
		for(int i=0;i<100;i++) {
			// ---------------- add new kid ----------------
			random_name = names[new Random().nextInt(names.length)]; // generate random kid's name
			new_kid = kidRepo.addNewKid(new Kid(random_name,new Date((new Date()).getTime()- ((int)Math.floor(Math.random()*(365)+1))*DAY_IN_MS),gender_arr[new Random().nextInt(gender_arr.length)])); // add new kid to kid repository
			if(i<15) {
				random_date = new Date((new Date()).getTime()- ((int)Math.floor(Math.random()*(2*365)+1))*DAY_IN_MS); // generate random time for meeting by year
			}
			else if(i>=15 & i<25) {
				random_date = new Date((new Date()).getTime()- ((int)Math.floor(Math.random()*(35)+1))*DAY_IN_MS); // generate random time for meeting by month
			}
			else if(i>=25 & i<40) {
				random_date = new Date((new Date()).getTime()- ((int)Math.floor(Math.random()*(7)+1))*DAY_IN_MS); // generate random time for meeting by week
			}
			kidRepo.setActiveDate(new_kid.getId(),random_date);  // change kid active date to random date
			kidRepo.addCourseToKid(new_kid.getId(), (allCourses.get(new Random().nextInt(allCourses.size()))).getID());  // add random course to kid
		}	 
	}
	
	// -------- get activivities per week/month/year --------  
	
	@GetMapping("getactivitiesperweek")
	public HashMap<String, Double> activitiesWeek(){
		
		return meetingRepo.getActivityTime(1);
	}
	
	@GetMapping("getactivitiespermonth")
	public HashMap<String, Double> activitiesMonth(){
		return meetingRepo.getActivityTime(2);
	}

	@GetMapping("getactivitiesperyear")
	public HashMap<String, Double> activitiesYear(){
		return meetingRepo.getActivityTime(3);	
	}
	
	// -------- get percentage of activities per week/month/year -------- 
	
	@GetMapping("getpercentactivitiesperweek")
	public Double percentActivitiesWeek(){
		HashMap<String, Double> activitiesNum = meetingRepo.getActivityTime(1);
		return (Double.valueOf(activitiesNum.get("activityTime"))/Double.valueOf(activitiesNum.get("totalTime")))*100;
	}
		
	@GetMapping("getpercentactivitiespermonth")
	public Double percentActivitiesMonth(){
		HashMap<String, Double> activitiesNum = meetingRepo.getActivityTime(2);
		return (Double.valueOf(activitiesNum.get("activityTime"))/Double.valueOf(activitiesNum.get("totalTime")))*100;
	}
		
	@GetMapping("getpercentactivitiesperyear")
	public Double percentActivitiesYear(){
		HashMap<String, Double> activitiesNum = meetingRepo.getActivityTime(3);
		return (Double.valueOf(activitiesNum.get("activityTime"))/Double.valueOf(activitiesNum.get("totalTime")))*100;
	}
	
	
	// -------- get active kids per week/month/year --------  
	
	@GetMapping("getlistofactivekidsperweek")
	public HashMap<String, Integer> activeKidsWeek(){
		
		return kidRepo.getNewKids(1);
	}
	
	@GetMapping("getlistofactivekidspermonth")
	public HashMap<String, Integer> activeKidsMonth(){
		return kidRepo.getNewKids(2);
	}

	@GetMapping("getlistofactivekidsperyear")
	public HashMap<String, Integer> activeKidsYear(){
		return kidRepo.getNewKids(3);
	}
	
	// -------- get percentage of active kids per week/month/year -------- 
	
	@GetMapping("getpercentactivekidsbyweek")
	public Double percentActiveKidsWeek(){
		HashMap<String, Integer> kidsNum = kidRepo.getNewKids(1);
		return (Double.valueOf(kidsNum.get("newKids"))/Double.valueOf(kidsNum.get("totalKids")))*100;
	}
	
	@GetMapping("getpercentactivekidsbymonth")
	public Double percentActiveKidsMonth(){
		HashMap<String, Integer> kidsNum = kidRepo.getNewKids(2);
		return (Double.valueOf(kidsNum.get("newKids"))/Double.valueOf(kidsNum.get("totalKids")))*100;
	}
	
	@GetMapping("getpercentactivekidsbyyear")
	public Double getpercentactivekidsbyyear(){
		HashMap<String, Integer> kidsNum = kidRepo.getNewKids(3);
		return (Double.valueOf(kidsNum.get("newKids"))/Double.valueOf(kidsNum.get("totalKids")))*100;
	}
	
	
	// -------- get active parents per week/month/year -------- 
	
	@GetMapping("getallactiveparentsbyweek")
	public HashMap<String, Integer> activeParentsWeek(){
		return parentRepo.getNewParents(1);
	}
	
	@GetMapping("getallactiveparentsbymonth")
	public HashMap<String, Integer> activeParentsMonth(){
		return parentRepo.getNewParents(2);
	}
	
	@GetMapping("getallactiveparentsbyyear")
	public HashMap<String, Integer> activeParentsYear(){
		return parentRepo.getNewParents(3);
	}
	
	
	// -------- get percentage of active kids per week/month/year --------
	
	@GetMapping("getpercentactiveparentsbyweek")
	public Double percentActiveParentsWeek(){
		HashMap<String, Integer> parentsNum = parentRepo.getNewParents(1);
		return (Double.valueOf(parentsNum.get("New Parents"))/Double.valueOf(parentsNum.get("totalParents")))*100;
	}
	
	@GetMapping("getpercentactiveparentsbymonth")
	public Double percentActiveParentsMonth(){
		HashMap<String, Integer> parentsNum = parentRepo.getNewParents(2);
		return (Double.valueOf(parentsNum.get("New Parents"))/Double.valueOf(parentsNum.get("totalParents")))*100;
	}
	
	@GetMapping("getpercentactiveparentsbyyear")
	public Double percentActiveParentsYear(){
		HashMap<String, Integer> parentsNum = parentRepo.getNewParents(3);
		return (Double.valueOf(parentsNum.get("New Parents"))/Double.valueOf(parentsNum.get("totalParents")))*100;
	}
	
	
	// -------- get active kids in category per week/month/year -------- 
	
	@GetMapping("getlistofactivekidspercategoryperweek")
	public HashMap<String, Integer> activeKidsCategWeek(){
		return categoryRepo.getKidsCountByCategory(1);
	}
	
	@GetMapping("getlistofactivekidspercategorypermonth")
	public HashMap<String, Integer> activeKidsCategMonth(){
		return categoryRepo.getKidsCountByCategory(2);
	}
	
	@GetMapping("getlistofactivekidspercategoryperyear")
	public HashMap<String, Integer> activeKidsCategYear(){
		return categoryRepo.getKidsCountByCategory(3);
	}
	
	@GetMapping("getkidsbycategorymonth/{category}")
	public TreeMap<Integer,Integer> kidsByCategoryMonth(@PathVariable String category){
		return kidRepo.getKidsCategoryMonth(category);
	}

		
}