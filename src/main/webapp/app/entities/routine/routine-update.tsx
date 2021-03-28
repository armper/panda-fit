import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IApplicationUser } from 'app/shared/model/application-user.model';
import { getEntities as getApplicationUsers } from 'app/entities/application-user/application-user.reducer';
import { getEntity, updateEntity, createEntity, reset } from './routine.reducer';
import { IRoutine } from 'app/shared/model/routine.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRoutineUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RoutineUpdate = (props: IRoutineUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { routineEntity, applicationUsers, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/routine');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getApplicationUsers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...routineEntity,
        ...values,
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
          <h2 id="pandaFitApp.routine.home.createOrEditLabel" data-cy="RoutineCreateUpdateHeading">
            Create or edit a Routine
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : routineEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="routine-id">ID</Label>
                  <AvInput id="routine-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="routine-name">
                  Name
                </Label>
                <AvField id="routine-name" data-cy="name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <Label id="dateStartedLabel" for="routine-dateStarted">
                  Date Started
                </Label>
                <AvField id="routine-dateStarted" data-cy="dateStarted" type="date" className="form-control" name="dateStarted" />
              </AvGroup>
              <AvGroup>
                <Label id="dateEndedLabel" for="routine-dateEnded">
                  Date Ended
                </Label>
                <AvField id="routine-dateEnded" data-cy="dateEnded" type="date" className="form-control" name="dateEnded" />
              </AvGroup>
              <AvGroup>
                <Label id="goalDateLabel" for="routine-goalDate">
                  Goal Date
                </Label>
                <AvField id="routine-goalDate" data-cy="goalDate" type="date" className="form-control" name="goalDate" />
              </AvGroup>
              <AvGroup>
                <Label id="startingBodyWeightLabel" for="routine-startingBodyWeight">
                  Starting Body Weight
                </Label>
                <AvField
                  id="routine-startingBodyWeight"
                  data-cy="startingBodyWeight"
                  type="string"
                  className="form-control"
                  name="startingBodyWeight"
                />
              </AvGroup>
              <AvGroup>
                <Label id="endingBodyWeightLabel" for="routine-endingBodyWeight">
                  Ending Body Weight
                </Label>
                <AvField
                  id="routine-endingBodyWeight"
                  data-cy="endingBodyWeight"
                  type="string"
                  className="form-control"
                  name="endingBodyWeight"
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/routine" replace color="info">
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
  applicationUsers: storeState.applicationUser.entities,
  routineEntity: storeState.routine.entity,
  loading: storeState.routine.loading,
  updating: storeState.routine.updating,
  updateSuccess: storeState.routine.updateSuccess,
});

const mapDispatchToProps = {
  getApplicationUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RoutineUpdate);
