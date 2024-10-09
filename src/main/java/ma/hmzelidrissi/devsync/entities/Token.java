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

    private int dailyReplaceTokens = 2;
    private int monthlyDeleteTokens = 1;
    private LocalDate lastResetDate;

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
