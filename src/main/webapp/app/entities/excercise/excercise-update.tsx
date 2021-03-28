import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRoutine } from 'app/shared/model/routine.model';
import { getEntities as getRoutines } from 'app/entities/routine/routine.reducer';
import { getEntity, updateEntity, createEntity, reset } from './excercise.reducer';
import { IExcercise } from 'app/shared/model/excercise.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IExcerciseUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ExcerciseUpdate = (props: IExcerciseUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { excerciseEntity, routines, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/excercise');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getRoutines();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...excerciseEntity,
        ...values,
        routine: routines.find(it => it.id.toString() === values.routineId.toString()),
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
          <h2 id="pandaFitApp.excercise.home.createOrEditLabel" data-cy="ExcerciseCreateUpdateHeading">
            Create or edit a Excercise
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : excerciseEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="excercise-id">ID</Label>
                  <AvInput id="excercise-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="typeLabel" for="excercise-type">
                  Type
                </Label>
                <AvInput
                  id="excercise-type"
                  data-cy="type"
                  type="select"
                  className="form-control"
                  name="type"
                  value={(!isNew && excerciseEntity.type) || 'BARBELL'}
                >
                  <option value="BARBELL">BARBELL</option>
                  <option value="BAR">BAR</option>
                  <option value="MACHINE">MACHINE</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="currentVolumeLabel" for="excercise-currentVolume">
                  Current Volume
                </Label>
                <AvField id="excercise-currentVolume" data-cy="currentVolume" type="string" className="form-control" name="currentVolume" />
              </AvGroup>
              <AvGroup>
                <Label id="startingVolumeLabel" for="excercise-startingVolume">
                  Starting Volume
                </Label>
                <AvField
                  id="excercise-startingVolume"
                  data-cy="startingVolume"
                  type="string"
                  className="form-control"
                  name="startingVolume"
                />
              </AvGroup>
              <AvGroup>
                <Label id="goalVolumeLabel" for="excercise-goalVolume">
                  Goal Volume
                </Label>
                <AvField id="excercise-goalVolume" data-cy="goalVolume" type="string" className="form-control" name="goalVolume" />
              </AvGroup>
              <AvGroup>
                <Label for="excercise-routine">Routine</Label>
                <AvInput id="excercise-routine" data-cy="routine" type="select" className="form-control" name="routineId">
                  <option value="" key="0" />
                  {routines
                    ? routines.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/excercise" replace color="info">
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
  routines: storeState.routine.entities,
  excerciseEntity: storeState.excercise.entity,
  loading: storeState.excercise.loading,
  updating: storeState.excercise.updating,
  updateSuccess: storeState.excercise.updateSuccess,
});

const mapDispatchToProps = {
  getRoutines,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ExcerciseUpdate);
