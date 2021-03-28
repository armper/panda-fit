import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './excercise.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IExcerciseDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ExcerciseDetail = (props: IExcerciseDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { excerciseEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="excerciseDetailsHeading">Excercise</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{excerciseEntity.id}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{excerciseEntity.type}</dd>
          <dt>
            <span id="currentVolume">Current Volume</span>
          </dt>
          <dd>{excerciseEntity.currentVolume}</dd>
          <dt>
            <span id="startingVolume">Starting Volume</span>
          </dt>
          <dd>{excerciseEntity.startingVolume}</dd>
          <dt>
            <span id="goalVolume">Goal Volume</span>
          </dt>
          <dd>{excerciseEntity.goalVolume}</dd>
          <dt>Routine</dt>
          <dd>{excerciseEntity.routine ? excerciseEntity.routine.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/excercise" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/excercise/${excerciseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ excercise }: IRootState) => ({
  excerciseEntity: excercise.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ExcerciseDetail);
