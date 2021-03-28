import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './application-user.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IApplicationUserDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ApplicationUserDetail = (props: IApplicationUserDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { applicationUserEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="applicationUserDetailsHeading">ApplicationUser</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{applicationUserEntity.id}</dd>
          <dt>
            <span id="ranking">Ranking</span>
          </dt>
          <dd>{applicationUserEntity.ranking}</dd>
          <dt>Internal User</dt>
          <dd>{applicationUserEntity.internalUser ? applicationUserEntity.internalUser.id : ''}</dd>
          <dt>Routine</dt>
          <dd>
            {applicationUserEntity.routines
              ? applicationUserEntity.routines.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {applicationUserEntity.routines && i === applicationUserEntity.routines.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/application-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/application-user/${applicationUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ applicationUser }: IRootState) => ({
  applicationUserEntity: applicationUser.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ApplicationUserDetail);
