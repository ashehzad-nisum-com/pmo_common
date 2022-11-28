package com.pmo.common.exception;

import com.pmo.common.dto.ApiResponseDTO;
import com.pmo.common.dto.ErrorInfoDTO;
import com.pmo.common.enums.PmoErrors;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.pmo.common.enums.PmoErrors.PMO_ERRORS;


@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<ApiResponseDTO<Void>> handleError(ApplicationException e) {
        ApiResponseDTO<Void> response = new ApiResponseDTO<>(new ArrayList<>(), null);
        response.getErrors().add(new ErrorInfoDTO(e.getCode(), e.getMessage()));
        HttpStatus status = HttpStatus.BAD_REQUEST;
        logger.info("Handling ApplicationException" + e);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler({InvalidDataAccessResourceUsageException.class})
    public ResponseEntity<Object> handleError(InvalidDataAccessResourceUsageException e) {
        ApiResponseDTO<Void> response = new ApiResponseDTO<>(new ArrayList<>(), null);
        response.getErrors().add(new ErrorInfoDTO(PmoErrors.PMO_ERRORS.getCode(), "Error occurred please" +
            " contact support"));
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        logger.info("Handling InvalidDataAccessResourceUsageException");
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler({ApplicationAccessException.class})
    public ResponseEntity<ApiResponseDTO<Void>> handleError(ApplicationAccessException e) {
        ApiResponseDTO<Void> response = new ApiResponseDTO<>(new ArrayList<>(), null);
        response.getErrors().add(new ErrorInfoDTO(e.getCode(), e.getMessage()));
        HttpStatus status = HttpStatus.BAD_REQUEST;
        logger.info("Handling ApplicationAccessException");
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler({InternalServerErrorException.class})
    public ResponseEntity<ApiResponseDTO<Void>> handleError(InternalServerErrorException e) {
        ApiResponseDTO<Void> response = new ApiResponseDTO<>(new ArrayList<>(), null);
        response.getErrors().add(new ErrorInfoDTO(PMO_ERRORS.getCode(), e.getMessage()));
        HttpStatus status = HttpStatus.BAD_REQUEST;
        logger.info("Handling InternalServerErrorException");
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException ex) {
        final List<ErrorInfoDTO> errors = new ArrayList<>();

        logger.error(ex.getClass().getName() + ": " + ex.getMessage());
        errors.add(new ErrorInfoDTO(PMO_ERRORS.getCode(), ex.getMessage()));

        final ApiResponseDTO<Void> response = new ApiResponseDTO<>(errors, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    //400

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers, final HttpStatus status,
                                                                  final WebRequest request) {
        //
        final List<ErrorInfoDTO> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            logger.error(ex.getClass().getName() + " " + error.getField() + ": " + error.getDefaultMessage());
            errors.add(new ErrorInfoDTO(PMO_ERRORS.getCode(),
                error.getField() + ": " + error.getDefaultMessage()));
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            logger.error(ex.getClass().getName() + " " + error.getObjectName() + ": " + error.getDefaultMessage());
            errors.add(new ErrorInfoDTO(PMO_ERRORS.getCode(),
                error.getObjectName() + ": " + error.getDefaultMessage()));
        }

        final ApiResponseDTO<Void> response = new ApiResponseDTO<>(errors, null);
        return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers,
                                                         final HttpStatus status, final WebRequest request) {
        //
        final List<ErrorInfoDTO> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            logger.error(ex.getClass().getName() + " " + error.getField() + ":  " + error.getDefaultMessage());
            errors.add(new ErrorInfoDTO(PMO_ERRORS.getCode(), error.getField() + " is Invalid"));
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            logger.error(ex.getClass().getName() + " " + error.getObjectName() + " : " + error.getDefaultMessage());
            errors.add(new ErrorInfoDTO(PMO_ERRORS.getCode(), error.getObjectName() + " is Invalid"));
        }
        final ApiResponseDTO<Void> response = new ApiResponseDTO<>(errors, null);
        return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers,
                                                        final HttpStatus status, final WebRequest request) {
        //
        final String error =
            ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();
        logger.error(ex.getClass().getName() + " :" + error);

        final ApiResponseDTO<Void> response = new ApiResponseDTO<>(new ArrayList<>(), null);
        response.getErrors().add(new ErrorInfoDTO(PMO_ERRORS.getCode(), ex.getPropertyName() + " is invalid"));
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex
        , final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        //
        final String error = ex.getRequestPartName() + " part is missing";
        logger.error(ex.getClass().getName() + " : " + error);

        final ApiResponseDTO<Void> response = new ApiResponseDTO<>(new ArrayList<>(), null);
        response.getErrors().add(new ErrorInfoDTO(PMO_ERRORS.getCode(), error));
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex
        , final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        //
        final String error = ex.getParameterName() + " is required";
        logger.error(ex.getClass().getName() + " :  " + error);

        final ApiResponseDTO<Void> response = new ApiResponseDTO<>(new ArrayList<>(), null);
        response.getErrors().add(new ErrorInfoDTO(PMO_ERRORS.getCode(), error));
        logger.info("Handling ApplicationException" + ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex,
                                                                   final WebRequest request) {
        //
        final String error = ex.getName() + " should be of type " + Objects.requireNonNull(ex.getRequiredType()).getName();
        logger.error(ex.getClass().getName() + " " + error);

        final ApiResponseDTO<Void> response = new ApiResponseDTO<>(new ArrayList<>(), null);
        response.getErrors().add(new ErrorInfoDTO(PMO_ERRORS.getCode(), ex.getName().concat(" is invalid")));
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    // 404
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex
        , final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        //
        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        logger.error(ex.getClass().getName() + "  " + error);

        final ApiResponseDTO<Void> response = new ApiResponseDTO<>(new ArrayList<>(), null);
        response.getErrors().add(new ErrorInfoDTO(PMO_ERRORS.getCode(), error));
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    // 405
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex
        , final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        //
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> builder.append(t + " "));
        logger.error(ex.getClass().getName() + " " + builder);

        final ApiResponseDTO<Void> response = new ApiResponseDTO<>(new ArrayList<>(), null);
        response.getErrors().add(new ErrorInfoDTO(PMO_ERRORS.getCode(), builder.toString()));
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    // 415
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex
        , final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        //
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));
        logger.error(ex.getClass().getName() + " " + builder);

        final ApiResponseDTO<Void> response = new ApiResponseDTO<>(new ArrayList<>(), null);
        response.getErrors().add(new ErrorInfoDTO(PMO_ERRORS.getCode(), builder.substring(0,
            builder.length() - 2)));
        return new ResponseEntity<>(request, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    // 500

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(final Exception ex) {
        logger.error(ex.getClass().getName());
        logger.error("error", ex);
        //

        final ApiResponseDTO<Void> response = new ApiResponseDTO<>(new ArrayList<>(), null);
        response.getErrors().add(new ErrorInfoDTO(PMO_ERRORS.getCode(), "Error occurred please contact " +
            "support"));
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NoContentException.class})
    public ResponseEntity<ApiResponseDTO<Void>> handleNoContentException(NoContentException e) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        return ResponseEntity.status(status).body(new ApiResponseDTO<>(null, null));
    }
}
