package knewone.healthcaresb;

import knewone.healthcaresb.doctor.persistence.DoctorService;
import knewone.healthcaresb.doctor.persistence.entity.Doctor;
import knewone.healthcaresb.patient.PatientService;
import knewone.healthcaresb.patient.persistence.entity.Patient;
import knewone.healthcaresb.visit.VisitService;
import knewone.healthcaresb.visit.communication.dto.CreateVisitRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@RequiredArgsConstructor
class HealthcareSbApplicationTests {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private VisitService visitService;

    private final Random random = new Random();

    // Flavour for generator
    private static final List<ZoneId> TIMEZONES = List.of(
            ZoneId.of("Atlantic/Reykjavik"),
            ZoneId.of("Europe/London"),
            ZoneId.of("Europe/Berlin"),
            ZoneId.of("Europe/Moscow"),
            ZoneId.of("America/New_York"),
            ZoneId.of("America/Los_Angeles"),
            ZoneId.of("Asia/Tokyo"),
            ZoneId.of("Asia/Kolkata")
    );

    List<String> firstNames = List.of("Paisley", "Sophie", "Zoey", "Adeline", "Sadie", "Josephine", "Genesis", "Lillian", "Leah", "Hannah", "Maya", "Delilah", "Stella", "Victoria", "Valentina", "Madison", "Elena", "Naomi", "Emilia", "Riley", "Willow", "Grace", "Nova", "Lainey", "Layla", "Ivy", "Isla", "Lucy", "Mila", "Abigail", "Avery", "Ella", "Zoe", "Penelope", "Scarlett", "Aria", "Emily", "Lily", "Gianna", "Nora", "Ellie", "Chloe", "Hazel", "Eliana", "Elizabeth", "Aurora", "Violet", "Eleanor", "Luna", "Harper", "Camila", "Sofia", "Ava", "Evelyn", "Isabella", "Sophia", "Mia", "Charlotte", "Amelia", "Emma", "Olivia"
    );

    List<String> lastNames = List.of("Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzales", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores", "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter", "Roberts", "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker", "Cruz", "Edwards", "Collins", "Reyes", "Stewart", "Morris", "Morales", "Murphy", "Cook", "Rogers", "Gutierrez", "Ortiz", "Morgan", "Cooper", "Peterson", "Bailey", "Reed", "Kelly", "Howard", "Ramos", "Kim", "Cox", "Ward", "Richardson", "Watson", "Brooks", "Chavez", "Wood", "James", "Bennet", "Gray", "Mendoza", "Ruiz", "Hughes", "Price", "Alvarez", "Castillo", "Sanders", "Patel", "Myers", "Long", "Ross", "Foster", "Jimenez"
    );

    private String getFN() {
        return firstNames.get(random.nextInt(firstNames.size()));
    }

    private String getLN() {
        return lastNames.get(random.nextInt(lastNames.size()));
    }

    @Test
    public void generateTestData() {
        Random rand = new Random();

        // Create doctors with random time zones
        List<Doctor> doctors = IntStream.range(1, 5)
                .mapToObj(i -> new Doctor(getFN(), getLN(), TIMEZONES.get(rand.nextInt(TIMEZONES.size()))))
                .map(doctorService::save)
                .collect(Collectors.toList());

        // Create patients
        List<Patient> patients = IntStream.range(1, 10)
                .mapToObj(i -> new Patient(getFN(), getLN()))
                .map(patientService::save)
                .toList();

        // Create visits
        for (Patient patient : patients) {
            Collections.shuffle(doctors);
            int visitsCount = 1 + rand.nextInt(3);
            for (int i = 0; i < visitsCount; i++) {
                Doctor doctor = doctors.get(i);

                LocalDate randomDate = LocalDate.now().minusDays(rand.nextInt(30));
                int hour = 9 + rand.nextInt(9);

                ZoneId doctorZone = doctor.getTimezone();
                ZonedDateTime startZoned = ZonedDateTime.of(randomDate, LocalTime.of(hour, 0), doctorZone);
                ZonedDateTime endZoned = startZoned.plusHours(1);

                CreateVisitRequest request = new CreateVisitRequest();
                request.setPatientId(patient.getId());
                request.setDoctorId(doctor.getId());
                request.setStart(startZoned);
                request.setEnd(endZoned);

                visitService.createVisit(request);
            }
        }

        System.out.println("Test data generation complete!");
    }
}
