package nextstep.subway.path.domain;

import java.util.List;
import java.util.Optional;
import nextstep.subway.exception.NotLinkedPathException;
import nextstep.subway.exception.SamePathException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);
    private DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = null;

    public void init(List<Line> lines) {
        lines.forEach(this::addVertexAndEdge);
    }

    private void addVertexAndEdge(Line line) {
        addVertex(line.getSections());
        addEdgeWeight(line.getSections());
    }

    private void addVertex(Sections sections) {
        sections.getStations()
                .forEach(graph::addVertex);
    }

    private void addEdgeWeight(Sections sections) {
        sections.getSections()
                .forEach(it -> graph.setEdgeWeight(addEdge(it), weight(it.getDistance())));
    }

    private double weight(Distance distance) {
        return distance.getDistance();
    }

    private DefaultWeightedEdge addEdge(Section section) {
        return graph.addEdge(section.upStation(), section.downStation());
    }

    public GraphPath<Station, DefaultWeightedEdge> getDijkstraPath(Station source, Station target) {
        validateSameSourceAndTarget(source, target);
        return getOptionalDijkstraPath(source, target).orElseThrow(NotLinkedPathException::new);
    }

    public Optional<GraphPath<Station, DefaultWeightedEdge>> getOptionalDijkstraPath(Station source, Station target) {
        if (dijkstraShortestPath == null) {
            dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        }
        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target));
    }

    private void validateSameSourceAndTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new SamePathException();
        }
    }

    List<Station> getShortestPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> dijkstraPath = getDijkstraPath(source, target);
        return dijkstraPath.getVertexList();
    }

    int getShortestDistance(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> dijkstraPath = getDijkstraPath(source, target);
        return (int) dijkstraPath.getWeight();
    }
}
