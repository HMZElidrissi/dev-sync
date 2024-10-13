package ma.hmzelidrissi.devsync.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    private int dailyReplaceTokens;
    private int monthlyDeleteTokens;
    private LocalDate lastResetDate;

    public Token() {
        this.dailyReplaceTokens = 2;
        this.monthlyDeleteTokens = 1;
        this.lastResetDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getDailyReplaceTokens() {
        return dailyReplaceTokens;
    }

    public void setDailyReplaceTokens(int dailyReplaceTokens) {
        this.dailyReplaceTokens = dailyReplaceTokens;
    }

    public int getMonthlyDeleteTokens() {
        return monthlyDeleteTokens;
    }

    public void setMonthlyDeleteTokens(int monthlyDeleteTokens) {
        this.monthlyDeleteTokens = monthlyDeleteTokens;
    }

    public LocalDate getLastResetDate() {
        return lastResetDate;
    }

    public void setLastResetDate(LocalDate lastResetDate) {
        this.lastResetDate = lastResetDate;
    }
}
