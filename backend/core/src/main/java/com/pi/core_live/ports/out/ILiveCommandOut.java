package com.pi.core_live.ports.out;

import com.pi.core_live.core.domain.Live;
import com.pi.utils.exceptions.GlobalException;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * This interface represents the output port for the live service, which is
 * responsible for performing the commands related to the live class.
 *
 * @author  Gustavo Boaz
 * @version 1.0.0
 * @since   1.0.0
 */
public interface ILiveCommandOut {

    /**
     * Creates a new live class.
     *
     * @param loginTeacher The login of the teacher.
     * @param codeTeacher The code of the teacher.
     * @param keyQuiz The key of the quiz.
     * @return A Mono that emits the created live class.
     * @throws GlobalException if an error occurs during the process.
     */
    Mono<Live> createLive(final String loginTeacher, final String codeTeacher, final String keyQuiz) throws GlobalException;

    /**
     * Sets the next position of the live class.
     *
     * @param loginTeacher The login of the teacher.
     * @param codeTeacher The code of the teacher.
     * @param keyLive The key of the live class.
     * @return A Mono that emits the updated live class.
     * @throws GlobalException if an error occurs during the process.
     */
    Mono<Live> nextPosition(final String loginTeacher, final String codeTeacher, final String keyLive) throws GlobalException;

    /**
     * Sets the previous position of the live class.
     *
     * @param loginTeacher The login of the teacher.
     * @param codeTeacher The code of the teacher.
     * @param keyLive The key of the live class.
     * @return A Mono that emits the updated live class.
     * @throws GlobalException if an error occurs during the process.
     */
    Mono<Live> previousPosition(final String loginTeacher, final String codeTeacher, final String keyLive) throws GlobalException;

    /**
     * Adds a pupil to the lobby of the live class.
     *
     * @param loginPupil The login of the pupil.
     * @param codePupil The code of the pupil.
     * @param keyLive The key of the live class.
     * @return A Mono that emits the updated live class.
     * @throws GlobalException if an error occurs during the process.
     */
    Mono<Live> addPupilToLobby(final String loginPupil, final String codePupil, final String keyLive) throws GlobalException;

    /**
     * Removes a pupil from the lobby of the live class.
     *
     * @param loginTeacher The login of the teacher.
     * @param codeTeacher The code of the teacher.
     * @param loginPupil The login of the pupil.
     * @param codePupil The code of the pupil.
     * @param keyLive The key of the live class.
     * @return A Mono that emits the updated live class.
     * @throws GlobalException if an error occurs during the process.
     */
    Mono<Live> removePupilFromLobby(final String loginTeacher, final String codeTeacher, final String loginPupil, final String codePupil, final String keyLive) throws GlobalException;

    /**
     * Adds an answer from a pupil to the live class.
     *
     * @param loginPupil The login of the pupil.
     * @param codePupil The code of the pupil.
     * @param keyLive The key of the live class.
     * @param answerItem The list answer of the pupil.
     * @return A Mono that emits the updated live class.
     * @throws GlobalException if an error occurs during the process.
     */
    Mono<Live> addPupilAnswerToQuiz(final String loginPupil, final String codePupil, final String keyLive, final List<String> answerItem) throws GlobalException;

    /**
     * Ends the live class.
     *
     * @param loginTeacher The login of the teacher.
     * @param codeTeacher The code of the teacher.
     * @param keyLive The key of the live class.
     * @return A Mono that emits the updated live class.
     * @throws GlobalException if an error occurs during the process.
     */
    Mono<Live> endLive(final String loginTeacher, final String codeTeacher, final String keyLive) throws GlobalException;

    /**
     * Persists the live class.
     *
     * @param keyLive The key of the live class.
     * @return A Mono that emits the persisted live class.
     * @throws GlobalException if an error occurs during the process.
     */
    Mono<Live> persistLive(final String keyLive) throws GlobalException;
}
