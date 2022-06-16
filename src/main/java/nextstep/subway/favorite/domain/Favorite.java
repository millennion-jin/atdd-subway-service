package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOURCE_STATION_ID", foreignKey = @ForeignKey(name = "fk_Favorite_Source_Station"))
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TARGET_STATION_ID", foreignKey = @ForeignKey(name = "fk_Favorite_Target_Station"))
    private Station target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(name = "fk_Favorite_Member"))
    private Member member;

    protected Favorite() {
    }

    public Favorite(Station source, Station target, Member member) {
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
