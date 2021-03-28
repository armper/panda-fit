import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IExcercise } from 'app/shared/model/excercise.model';
import { getEntities as getExcercises } from 'app/entities/excercise/excercise.reducer';
import { getEntity, updateEntity, createEntity, reset } from './cycle.reducer';
import { ICycle } from 'app/shared/model/cycle.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICycleUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CycleUpdate = (props: ICycleUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { cycleEntity, excercises, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/cycle');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getExcercises();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...cycleEntity,
        ...values,
        excercise: excercises.find(it => it.id.toString() === values.excerciseId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pandaFitApp.cycle.home.createOrEditLabel" data-cy="CycleCreateUpdateHeading">
            Create or edit a Cycle
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : cycleEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="cycle-id">ID</Label>
                  <AvInput id="cycle-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="repsLabel" for="cycle-reps">
                  Reps
                </Label>
                <AvField id="cycle-reps" data-cy="reps" type="string" className="form-control" name="reps" />
              </AvGroup>
              <AvGroup>
                <Label id="volumeLabel" for="cycle-volume">
                  Volume
                </Label>
                <AvField id="cycle-volume" data-cy="volume" type="string" className="form-control" name="volume" />
              </AvGroup>
              <AvGroup>
                <Label for="cycle-excercise">Excercise</Label>
                <AvInput id="cycle-excercise" data-cy="excercise" type="select" className="form-control" name="excerciseId">
                  <option value="" key="0" />
                  {excercises
                    ? excercises.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/cycle" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  excercises: storeState.excercise.entities,
  cycleEntity: storeState.cycle.entity,
  loading: storeState.cycle.loading,
  updating: storeState.cycle.updating,
  updateSuccess: storeState.cycle.updateSuccess,
});

const mapDispatchToProps = {
  getExcercises,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CycleUpdate);
