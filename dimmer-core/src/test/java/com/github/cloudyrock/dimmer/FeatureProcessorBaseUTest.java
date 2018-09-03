package com.github.cloudyrock.dimmer;


import com.github.cloudyrock.dimmer.exceptions.DummyExceptionWithFeatureInvocation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.UUID;
import java.util.function.Function;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class FeatureProcessorBaseUTest {
    @Rule public ExpectedException exception = ExpectedException.none();

    private final String operation = "operation";


    protected DummyFeatureProcessor dimmerProcessor;

    @Mock
    private Function<FeatureInvocation, String> behaviour;

    @Mock
    private FeatureInvocation featureInvocationMock;

    private String feature;

    @Before
    public void setUp() {

        dimmerProcessor = new DummyFeatureProcessor();
        feature = "FEATURE" + UUID.randomUUID().toString();
        initMocks(this);
        given(featureInvocationMock.getReturnType()).willReturn(String.class);
    }


    @Test
    @DisplayName("Should run behaviour when FEATURE when featureWithBehaviour")
    public void shouldRunBehaviour() throws Throwable {
        dimmerProcessor.featureWithBehaviour(feature, operation, s -> "VALUE");
        Object actualResult = dimmerProcessor.executeDimmerFeature(
                feature, operation, featureInvocationMock);
        assertEquals("VALUE", actualResult);
    }

    @Test
    @DisplayName("Should return value when featureWithValue")
    public void shouldReturnValue() throws Throwable {
        dimmerProcessor.featureWithValue(feature, operation, "VALUE");
        Object actualResult = dimmerProcessor.executeDimmerFeature(
                feature, operation, featureInvocationMock);
        assertEquals("VALUE", actualResult);
    }

    @Test
    @DisplayName("Should pass FeatureInvocation parameter when featureWithBehaviour")
    public void ensureFeatureInvocationParameterWhenBehaviour() throws Throwable {
        given(behaviour.apply(any(FeatureInvocation.class))).willReturn("not_checked");

        dimmerProcessor.featureWithBehaviour(feature, operation, behaviour);
        dimmerProcessor.executeDimmerFeature(feature, operation, featureInvocationMock);

        then(behaviour).should().apply(featureInvocationMock);
    }

    @Test
    @DisplayName("Should pass FeatureInvocation parameter when featureWithBehaviour")
    public void ensureFeatureInvocationParameterWhenException() throws Throwable {

        FeatureInvocation featureInvocationMock = mock(FeatureInvocation.class);
        given(behaviour.apply(any(FeatureInvocation.class))).willReturn("not_checked");
        

        dimmerProcessor.featureWithException(
                feature,
                operation,
                DummyExceptionWithFeatureInvocation.class);

        exception.expect(DummyExceptionWithFeatureInvocation.class);
        exception.expect(hasProperty("featureInvocation", is(featureInvocationMock)));

        dimmerProcessor.executeDimmerFeature(feature, operation, featureInvocationMock);
    }
}
