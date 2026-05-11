// package com.misrshoryanelataa.misr_shoryan_elataa;

// import com.misrshoryanelataa.misr_shoryan_elataa.Enums.*;
// import com.misrshoryanelataa.misr_shoryan_elataa.Models.*;
// import com.misrshoryanelataa.misr_shoryan_elataa.Repos.*;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// import java.sql.Date;
// import java.time.LocalDate;
// import java.time.LocalTime;
// import java.util.ArrayList;
// import java.util.List;

// @Component
// public class DataSeeder implements CommandLineRunner {

//     private final PrRepo prRepo;
//     private final HrRepo hrRepo;
//     private final VolunteerRepo volunteerRepo;
//     private final InterviewRepo interviewRepo;
//     private final InterviewSlotsRepo slotRepo;
//     private final CampaignRepo campaignRepo;
//     private final DonorRepo donorRepo;

//     public DataSeeder(PrRepo prRepo,
//                       HrRepo hrRepo,
//                       VolunteerRepo volunteerRepo,
//                       InterviewRepo interviewRepo,
//                       InterviewSlotsRepo slotRepo,
//                       CampaignRepo campaignRepo,
//                       DonorRepo donorRepo) {
//         this.prRepo = prRepo;
//         this.hrRepo = hrRepo;
//         this.volunteerRepo = volunteerRepo;
//         this.interviewRepo = interviewRepo;
//         this.slotRepo = slotRepo;
//         this.campaignRepo = campaignRepo;
//         this.donorRepo = donorRepo;
//     }

//     @Override
//     public void run(String... args) {

//         // ================= PR =================
//         PREntity pr = new PREntity();
//         pr.setName("PR Manager 1");
//         pr.setEmail("pr1@test.com");
//         pr.setPassword("123");
//         pr.setIsAdmin(true);
//         prRepo.save(pr);

//         // ================= HR =================
//         List<HREntity> hrList = new ArrayList<>();
//         for (int i = 1; i <= 5; i++) {
//             HREntity hr = new HREntity();
//             hr.setName("HR " + i);
//             hr.setEmail("hr" + i + "@test.com");
//             hr.setPassword("123");
//             hr.setIsAdmin(i == 1);
//             hrRepo.save(hr);
//             hrList.add(hr);
//         }

//         // ================= VOLUNTEERS =================
//         List<VolunteerEntity> volunteers = new ArrayList<>();
//         for (int i = 1; i <= 50; i++) {
//             VolunteerEntity v = new VolunteerEntity();
//             v.setName("Volunteer " + i);
//             v.setEmail("v" + i + "@mail.com");
//             v.setUniversityEmail("v" + i + "@med.asu.eg");
//             v.setPhoneNumber("01000000" + i);
//             v.setHr(hrList.get(i % hrList.size()));
//             v.setStatus(volunteerStatus.PENDING);
//             volunteerRepo.save(v);
//             volunteers.add(v);
//         }

//         // ================= INTERVIEWS =================
//         for (int i = 1; i <= 5; i++) {
//             InterviewEntity interview = new InterviewEntity();
//             interview.setName("Interview " + i);
//             interview.setHr(hrList.get(i % hrList.size()));
//             interviewRepo.save(interview);

//             // ================= SLOTS =================
//             for (int j = 1; j <= 20; j++) {
//                 InterviewSlotEntity slot = new InterviewSlotEntity();
//                 slot.setSlotDate(LocalDate.now().plusDays(j));
//                 slot.setSlotTime(LocalTime.of(10 + (j % 8), 0));
//                 slot.setStatus(InterviewStatus.AVAILABLE);
//                 slot.setInterview(interview);
//                 slotRepo.save(slot);
//             }
//         }

//         // ================= CAMPAIGNS =================
//         for (int i = 1; i <= 20; i++) {
//             CampaignEntity c = new CampaignEntity();
//             c.setTitle("Campaign " + i);
//             c.setDescription("Medical donation campaign " + i);
//             c.setLocation("Cairo " + i);
//             c.setDate(Date.valueOf(LocalDate.now().plusDays(i)));
//             c.setIsActivated(true);
//             c.setPr(pr);
//             campaignRepo.save(c);
//         }

//         // ================= DONORS =================
//         for (int i = 1; i <= 100; i++) {
//             DonorEntity d = new DonorEntity();
//             d.setName("Donor " + i);
//             d.setEmail("donor" + i + "@mail.com");
//             d.setPhone("01111111" + i);
//             d.setCity("Cairo");
//             d.setAge(18 + (i % 40));
//             d.setBloodType(BloodType.O_Positive);
//             d.setDonationType(DonationType.REPEAT);
//             d.setDonorstatus(volunteerStatus.PENDING);
//             donorRepo.save(d);
//         }

//         System.out.println("🔥 BIG DATA SET INSERTED SUCCESSFULLY");
//     }
// }