import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IRoutine } from 'app/shared/model/routine.model';
import { getEntities as getRoutines } from 'app/entities/routine/routine.reducer';
import { getEntity, updateEntity, createEntity, reset } from './application-user.reducer';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IApplicationUserUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ApplicationUserUpdate = (props: IApplicationUserUpdateProps) => {
  const [idsroutine, setIdsroutine] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { applicationUserEntity, users, routines, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/application-user');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
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
        ...applicationUserEntity,
        ...values,
        routines: mapIdList(values.routines),
        internalUser: users.find(it => it.id.toString() === values.internalUserId.toString()),
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
          <h2 id="pandaFitApp.applicationUser.home.createOrEditLabel" data-cy="ApplicationUserCreateUpdateHeading">
            Create or edit a ApplicationUser
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : applicationUserEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="application-user-id">ID</Label>
                  <AvInput id="application-user-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="rankingLabel" for="application-user-ranking">
                  Ranking
                </Label>
                <AvField id="application-user-ranking" data-cy="ranking" type="string" className="form-control" name="ranking" />
              </AvGroup>
              <AvGroup>
                <Label for="application-user-internalUser">Internal User</Label>
                <AvInput
                  id="application-user-internalUser"
                  data-cy="internalUser"
                  type="select"
                  className="form-control"
                  name="internalUserId"
                >
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="application-user-routine">Routine</Label>
                <AvInput
                  id="application-user-routine"
                  data-cy="routine"
                  type="select"
                  multiple
                  className="form-control"
                  name="routines"
                  value={!isNew && applicationUserEntity.routines && applicationUserEntity.routines.map(e => e.id)}
                >
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
              <Button tag={Link} id="cancel-save" to="/application-user" replace color="info">
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
  users: storeState.userManagement.users,
  routines: storeState.routine.entities,
  applicationUserEntity: storeState.applicationUser.entity,
  loading: storeState.applicationUser.loading,
  updating: storeState.applicationUser.updating,
  updateSuccess: storeState.applicationUser.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getRoutines,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ApplicationUserUpdate);
