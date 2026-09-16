package hr.java.jpa.demo;

import hr.java.jpa.demo.model.Polaznik;
import hr.java.jpa.demo.model.ProgramObrazovanja;
import hr.java.jpa.demo.model.Upis;
import hr.java.jpa.demo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        createAndSaveEntities();
        int izbor = 0;
        do {

            System.out.println("Odaberite radnju:\n 1. Unos polaznika\n 2. Unos programa obrazovanja\n " +
                    "3. Upis polaznika na program\n" +
                    " 4. Prebaci polaznika na drugi program\n 5. Ispis podataka o polazniku\n 6. Izlaz");


            System.out.print("Odabir: ");
            izbor = Integer.parseInt(scanner.nextLine());

            switch (izbor) {
                case 1:
                    System.out.print("Unesite ime: ");
                    String ime = scanner.nextLine();
                    System.out.print("Unesite prezime: ");
                    String prezime = scanner.nextLine();
                    unesiPolaznika(ime, prezime);
                    break;
                case 2:
                    System.out.print("Naziv programa obrazovanja: ");
                    String program = scanner.nextLine();
                    System.out.print("Unesite CSVET: ");
                    int csvet = Integer.parseInt(scanner.nextLine());
                    unesiProgramObrazovanja(program, csvet);
                    break;
                case 3:
                    System.out.print("ID programa: ");
                    Long idProgram = Long.parseLong(scanner.nextLine());
                    System.out.print("ID polaznika: ");
                    Long idPolaznik = Long.parseLong(scanner.nextLine());
                    upisiPolaznika(idProgram, idPolaznik);
                    break;
                case 4:
                    System.out.print("ID polaznika: ");
                    Long idPolaznik2 = Long.parseLong(scanner.nextLine());
                    System.out.print("Program za premještaj: ");
                    Long idNoviProgram = Long.parseLong(scanner.nextLine());
                    prebaciPolaznika(idPolaznik2, idNoviProgram);
                    break;
                case 5:
                    System.out.print("ID programa: ");
                    Long idPrograma = Long.parseLong(scanner.nextLine());
                    ispisPodatakaOPolazniku(idPrograma);
                    break;
                case 6:
                    System.exit(0);
            }

        } while (izbor != 0);
        HibernateUtil.shutdown();
    }


    public static void createAndSaveEntities() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();

            Polaznik polaznik1 = new Polaznik("Ivo", "Ivić");
            Polaznik polaznik2 = new Polaznik("Ana", "Anić");
            Polaznik polaznik3 = new Polaznik("Marta", "Mirić");

            ProgramObrazovanja po1 = new ProgramObrazovanja("Backend na Javi",
                    300);

            ProgramObrazovanja po2 = new ProgramObrazovanja("Uvod UX dizajn",
                    250);

            ProgramObrazovanja po3 = new ProgramObrazovanja("Novi program",
                    270);

            Upis upis1 = new Upis(po1, polaznik1);
            Upis upis2 = new Upis(po2, polaznik1);
            Upis upis3 = new Upis(po1, polaznik1);


            polaznik1.addPO(po1);
            polaznik1.addPO(po2);
            polaznik2.addPO(po1);

            session.persist(po1);
            session.persist(po2);
            session.persist(po3);

            session.persist(polaznik1);
            session.persist(polaznik2);
            session.persist(polaznik3);

            session.persist(upis1);
            session.persist(upis2);
            session.persist(upis3);

            transaction.commit();
        }
    }

    public static void unesiPolaznika(String ime, String prezime) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Polaznik noviPolaznik = new Polaznik(ime, prezime);

            session.persist(noviPolaznik);
            transaction.commit();
        }
    }

    public static void unesiProgramObrazovanja(String naziv, int csvet) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            ProgramObrazovanja po = new ProgramObrazovanja(naziv,
                    csvet);

            session.persist(po);
            transaction.commit();
        }
    }


    public static void upisiPolaznika(Long IDProgram, Long IDPolaznik) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            ProgramObrazovanja po = session.get(ProgramObrazovanja.class, IDProgram);
            Polaznik polaznik = session.get(Polaznik.class, IDPolaznik);

            if (polaznik == null || po == null) {
                System.err.println("Polaznik ili program s ponuđenim ID-em ne postoji.");
                transaction.rollback();
            }
            Upis noviUpis = new Upis(po, polaznik);

            session.persist(noviUpis);
            transaction.commit();
        }
    }

    public static void prebaciPolaznika(Long idPolaznik, Long idNoviProgram) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Polaznik polaznik = session.get(Polaznik.class, idPolaznik);
            ProgramObrazovanja noviPo = session.get(ProgramObrazovanja.class, idNoviProgram);

            String hql = "DELETE FROM Upis WHERE polaznik = :polaznik";
            session.createMutationQuery(hql).setParameter("polaznik", polaznik).executeUpdate();

            if (polaznik == null || noviPo == null) {
                System.err.println("Polaznik ili program s ponuđenim ID-em ne postoji.");
                transaction.rollback();
                return;
            }

            Upis noviUpis = new Upis(noviPo, polaznik);
            session.persist(noviUpis);

            transaction.commit();
        }
    }

    public static void ispisPodatakaOPolazniku(Long idProgram) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Upis> upisi = session.createQuery(
                            "FROM Upis u WHERE u.programObrazovanja.id = :idProgram", Upis.class)
                    .setParameter("idProgram", idProgram)
                    .list();

            if (upisi.isEmpty()) {
                System.err.println("Ne postoji program obrazovanja za ovaj ID, ili nema upisanih polaznika.");
                return;
            }
            System.out.printf("%s %s %s %s\n", "Ime", "Prezime", "Upisani program", "CSVET");
            System.out.printf("%s \n", "|--------------------------------------|");
            for (Upis upis : upisi) {
                Polaznik polaznik = upis.getPolaznik();
                ProgramObrazovanja program = upis.getProgramObrazovanja();
                System.out.printf("%s %s %15s %5d\n", polaznik.getIme(), polaznik.getPrezime(), program.getNaziv(),
                        program.getCsvet());

            }
            System.out.printf("%s \n", "|--------------------------------------|");
        }
    }

}