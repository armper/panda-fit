import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './routine.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRoutineDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RoutineDetail = (props: IRoutineDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { routineEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="routineDetailsHeading">Routine</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{routineEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{routineEntity.name}</dd>
          <dt>
            <span id="dateStarted">Date Started</span>
          </dt>
          <dd>
            {routineEntity.dateStarted ? <TextFormat value={routineEntity.dateStarted} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="dateEnded">Date Ended</span>
          </dt>
          <dd>
            {routineEntity.dateEnded ? <TextFormat value={routineEntity.dateEnded} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="goalDate">Goal Date</span>
          </dt>
          <dd>
            {routineEntity.goalDate ? <TextFormat value={routineEntity.goalDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="startingBodyWeight">Starting Body Weight</span>
          </dt>
          <dd>{routineEntity.startingBodyWeight}</dd>
          <dt>
            <span id="endingBodyWeight">Ending Body Weight</span>
          </dt>
          <dd>{routineEntity.endingBodyWeight}</dd>
        </dl>
        <Button tag={Link} to="/routine" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/routine/${routineEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ routine }: IRootState) => ({
  routineEntity: routine.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RoutineDetail);
