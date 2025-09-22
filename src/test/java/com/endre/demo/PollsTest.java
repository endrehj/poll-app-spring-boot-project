package com.endre.demo;

import com.endre.demo.domain.Poll;
import com.endre.demo.domain.User;
import com.endre.demo.domain.Vote;
import com.endre.demo.domain.VoteOption;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PollsTest {

    private EntityManagerFactory emf;

    private void runInTx(java.util.function.Consumer<EntityManager> work) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            work.accept(em);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private void populate(EntityManager em) {
        User alice = new User();
        alice.setUsername("alice");
        alice.setEmail("alice@online.com");

        User bob = new User();
        bob.setUsername("bob");
        bob.setEmail("bob@bob.home");

        User eve = new User();
        eve.setUsername("eve");
        eve.setEmail("eve@mail.org");

        em.persist(alice);
        em.persist(bob);
        em.persist(eve);

        Poll poll1 = new Poll();
        poll1.setQuestion("Vim or Emacs?");
        poll1.setCreator(alice);
        em.persist(poll1);

        VoteOption vim = new VoteOption();
        vim.setCaption("Vim");
        vim.setPresentationOrder(0);
        vim.setPoll(poll1);
        em.persist(vim);

        VoteOption emacs = new VoteOption();
        emacs.setCaption("Emacs");
        emacs.setPresentationOrder(1);
        emacs.setPoll(poll1);
        em.persist(emacs);

        Vote v1 = new Vote();
        v1.setVoter(alice);
        v1.setOption(vim);
        em.persist(v1);

        Vote v2 = new Vote();
        v2.setVoter(bob);
        v2.setOption(vim);
        em.persist(v2);

        Vote v3 = new Vote();
        v3.setVoter(eve);
        v3.setOption(emacs);
        em.persist(v3);

        Poll poll2 = new Poll();
        poll2.setQuestion("Pineapple on Pizza");
        poll2.setCreator(eve);
        em.persist(poll2);

        VoteOption yes = new VoteOption();
        yes.setCaption("Yes! Yammy!");
        yes.setPresentationOrder(0);
        yes.setPoll(poll2);
        em.persist(yes);

        VoteOption no = new VoteOption();
        no.setCaption("Mamma mia: Nooooo!");
        no.setPresentationOrder(1);
        no.setPoll(poll2);
        em.persist(no);

        Vote v4 = new Vote();
        v4.setVoter(eve);
        v4.setOption(yes);
        em.persist(v4);
    }

    @BeforeEach
    public void setUp() {
        Map<String, Object> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.driver", "org.h2.Driver");
        props.put("jakarta.persistence.jdbc.url", "jdbc:h2:mem:polls;DB_CLOSE_DELAY=-1");
        props.put("jakarta.persistence.jdbc.user", "sa");
        props.put("jakarta.persistence.jdbc.password", "");
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.put("hibernate.hbm2ddl.auto", "drop-and-create");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");

        emf = Persistence.createEntityManagerFactory("polls", props);

        runInTx(this::populate);
    }

    @Test
    public void testUsers() {
        runInTx(em -> {
            Long actual = em.createQuery("select count(u) from User u", Long.class)
                    .getSingleResult();
            assertEquals(3L, actual);

            User maybeBob = em.createQuery(
                            "select u from User u where u.username = :name", User.class)
                    .setParameter("name", "bob")
                    .getResultStream().findFirst().orElse(null);
            assertNotNull(maybeBob);
        });
    }

    @Test
    public void testVotes() {
        runInTx(em -> {
            Long vimVotes = em.createQuery(
                            "select count(v) from Vote v join v.option o join o.poll p join p.creator u " +
                                    "where u.email = :mail and o.presentationOrder = :ord", Long.class)
                    .setParameter("mail", "alice@online.com")
                    .setParameter("ord", 0)
                    .getSingleResult();

            Long emacsVotes = em.createQuery(
                            "select count(v) from Vote v join v.option o join o.poll p join p.creator u " +
                                    "where u.email = :mail and o.presentationOrder = :ord", Long.class)
                    .setParameter("mail", "alice@online.com")
                    .setParameter("ord", 1)
                    .getSingleResult();

            assertEquals(2L, vimVotes);
            assertEquals(1L, emacsVotes);
        });
    }

    @Test
    public void testOptions() {
        runInTx(em -> {
            List<String> poll2Options = em.createQuery(
                            "select o.caption from Poll p join p.options o join p.creator u " +
                                    "where u.email = :mail order by o.presentationOrder", String.class)
                    .setParameter("mail", "eve@mail.org")
                    .getResultList();

            assertEquals(List.of("Yes! Yammy!", "Mamma mia: Nooooo!"), poll2Options);
        });
    }
}
