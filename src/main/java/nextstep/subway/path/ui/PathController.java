package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity findShortestPath(@AuthenticationPrincipal LoginMember loginMember, @RequestParam(name = "source") Long source, @RequestParam(name = "target") Long target) {
        PathResponse pathResponse = pathService.findShortestPath(loginMember, source, target);

        return ResponseEntity.ok().body(pathResponse);
    }

    @ExceptionHandler({
            SameSourceTargetException.class, SourceTargetNotConnectException.class
    })
    public ResponseEntity exceptionHandler() {
        return ResponseEntity.badRequest().build();
    }

}
